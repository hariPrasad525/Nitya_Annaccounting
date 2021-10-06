//
//  CacheUpdater.m
//  Accounter
//
//  Created by krishnaiah on 4/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CacheUpdater.h"

@implementation CacheUpdater

- (id)init
{
    self = [super init];
    if (self) {
        mPath = [[NSString alloc]init];
        fileItems = [[NSArray alloc]init];
        initialFile = false;
    }
    return self;
}

-(void)initUpdating:(NSString *)urlPath{
    
    
    mPath = urlPath;
    
    NSURL *url = [[NSURL alloc]initWithString:[mPath stringByAppendingString:@"main/cachefiles"]];
    
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
    [request setDelegate:self];
    [request startAsynchronous];
    
    
}

-(void)versionVerify:(NSString*)versionNumber{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    if([prefs objectForKey:@"cacheVersion"]== nil){
        [self startUpdating:versionNumber];
    }else{
        NSString *value = [prefs objectForKey:@"cacheVersion"];
        if(![value isEqualToString:versionNumber]){
            [self startUpdating:versionNumber]; 
        }
    }
}


-(void)startUpdating:(NSString*)versionNumber{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [prefs setObject:versionNumber forKey:@"cacheVersion"];
    [prefs synchronize];
    
    
    countNumber = 0;
    progress = 0.0f;
    
    alert = [[UIAlertView alloc]initWithTitle:@"Updating Accounter" message:@"Please wait while Accounter is updated" delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
    
    UIActivityIndicatorView * activityIndicator = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake(12.0, 100.0, 260.0, 30.0) ] ; 
    [activityIndicator setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhite];
    [activityIndicator startAnimating];
    
    [alert addSubview:activityIndicator];
    [alert show];
    
    
    [self downLoadFileFromList:fileItems :mPath];
    
    
}

-(void)downLoadFileFromList:(NSArray*)fileArray:(NSString*)path{
    
    
    [[ NSFileManager defaultManager ] removeItemAtPath:folderPath error:nil];
    [ [ NSFileManager defaultManager ] createDirectoryAtPath: folderPath withIntermediateDirectories: YES attributes: nil error: NULL ];
    
    for (int i = 1; i<[fileArray count]-1; i++) {
        
        NSURL *url = [NSURL URLWithString:[path stringByAppendingString:[[fileArray objectAtIndex:i]substringWithRange:NSMakeRange(1, [[fileArray objectAtIndex:i] length]-1)]]];
        
        ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
        [request setDelegate:self];
        [request startAsynchronous];
        
    }
    
}
-(void)setView:(UIView*)view{
    mainView = view;
    
}

-(void)requestFinished:(ASIHTTPRequest *)request{
    
    NSLog(@"%d",[request responseStatusCode]);
    
    if(initialFile == FALSE){
        initialFile = TRUE;
        if([request responseStatusCode]==200){
            [self downLoadInitial:request];
        }
    }else {
        
        [self downLoadOther:request];
    }
    
    
    
}


-(void)downLoadInitial:(ASIHTTPRequest *)request{
    
    folderPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    [ [ NSFileManager defaultManager ] createDirectoryAtPath: folderPath withIntermediateDirectories: YES attributes: nil error: NULL ];
    
    NSData *data = [NSData dataWithContentsOfURL:[request url]];
    NSString *filedata= [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
    // NSLog(@"data : %@",filedata);
    fileItems = [filedata componentsSeparatedByString:@"\r\n"];
    
    
    NSString *versionNumber = [fileItems objectAtIndex:0];
    [self versionVerify:versionNumber];
    
    
}
-(void)downLoadOther:(ASIHTTPRequest *)request{
    countNumber++;
    folderPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString *path = [folderPath stringByAppendingString:[[request url]path]];
    
    NSFileManager *fileManager= [NSFileManager defaultManager]; 
    NSError* err = nil;
    
    [fileManager createDirectoryAtPath:[path stringByDeletingLastPathComponent] withIntermediateDirectories:YES attributes:	nil error:nil];
    
    NSData *data = [NSData dataWithContentsOfURL:[request url]];
    [data writeToFile:path options:NSAtomicWrite error:&err];
    if (err) {
        NSLog(@"oops: %@", err);
    }
    
    if(countNumber==17){
        
        alert.message =@"Updating Completed";
        [alert setHidden:TRUE];
        [alert dismissWithClickedButtonIndex:0 animated:TRUE];
    }
    
}



- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    
}
@end
