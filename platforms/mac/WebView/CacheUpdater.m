//
//  CacheUpdater.m
//  Accounter
//
//  Created by krishnaiah on 4/9/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CacheUpdater.h"

@implementation CacheUpdater



- (id)init
{
    self = [super init];
    if (self) {
        initialFile = false;
        fileItems = [[NSArray alloc]init];
    }
    return self;
}


-(void)setMainWindow:(NSWindow*)window:(NSPanel*)panel{
    mainWindow = window;
    viewPanel = panel;
}

/*
 create the ASIHTTPRequest for the cache main file which contains the list of files to be downloaded for saving to disk
 */
-(void)initUpdating:(NSString *)urlPath{
    
    mainPath = [[NSString alloc ]initWithString:urlPath];
    
    NSURL *url = [NSURL URLWithString:[mainPath stringByAppendingString:@"main/cache/mac"]];
    
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
    [request setDelegate:self];
    [request startAsynchronous];
    
    
}


/*
 To verify if the version of cache files is different that the saved. if it is different the new files will be downloaded by server or else
 noting wil be downloaded.
 */
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

/*
 start downloading the files and save the version number in NSUserDefaults
 */
-(void)startUpdating:(NSString*)versionNumber{
    
    NSLog(@"---Downloading new files---");
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [prefs setObject:versionNumber forKey:@"cacheVersion"];
    [prefs synchronize];
    
    [NSApp beginSheet:viewPanel modalForWindow:mainWindow modalDelegate:self didEndSelector: NULL contextInfo:nil];
    
    countNumber = 0;
    [self downLoadFileFromList:fileItems :mainPath];
    
    
}

-(void)downLoadFileFromList:(NSArray*)fileArray:(NSString*)path{
  

    
    [[ NSFileManager defaultManager ] removeItemAtPath:folderPath error:nil];
    [ [ NSFileManager defaultManager ] createDirectoryAtPath: folderPath withIntermediateDirectories: YES attributes: nil error: NULL ];
    
    for (int i = 1; i<[fileArray count]-2; i++) {
        
        NSString *fileobj = [fileArray objectAtIndex:i];
        NSURL *url = [NSURL URLWithString:[path stringByAppendingString:[fileobj substringWithRange:NSMakeRange(1, [fileobj length]-1)]]];
        NSLog(@"%@",[url absoluteString]);
        
        ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
        [request setDelegate:self];
        [request startAsynchronous];
    }
   // [queue go];

    
}
-(void)queueCompleted:(ASINetworkQueue*)queue{
    NSLog(@"comelpeted");
}
/*
 called by ASIHTTPRequest after each request is completed.
 */
-(void)requestFinished:(ASIHTTPRequest *)request{
    
    if(initialFile == FALSE){
        initialFile = TRUE;
        if([request responseStatusCode]==200){
            [self downLoadMain:request];
        }else {
            NSLog(@"cache files text not found");
        }
    }else {
        if([request responseStatusCode]==200){
              [self downLoadOthers:request];
        }else {
            NSLog(@"file downloading failed for : %@",[request url]);
        }
      
    }
}

-(void)closeView{
    [viewPanel orderOut:nil];
    [NSApp endSheet:viewPanel];
}

-(void)reuest:(ASIHTTPRequest *)request didReceiveData:(NSData *)data{
    
  
}

/*
 download first file
 */
-(void)downLoadMain:(ASIHTTPRequest*)request{
    
    NSString *filedata= [[NSString alloc]initWithContentsOfURL:[request url] encoding:NSUTF8StringEncoding error:NULL];
    if([filedata rangeOfString:@"\r\n"].length>0){
        fileItems = [filedata componentsSeparatedByString:@"\r\n"];
    }else if([filedata rangeOfString:@"\n"].length>0) {
        fileItems = [filedata componentsSeparatedByString:@"\n"];
    }
    NSLog(@"---Reading cache files----");
    
    if(fileItems.count>1){
        folderPath = [NSHomeDirectory() stringByAppendingFormat:@"/Library/Application Support/Accounter/"];
        [ [ NSFileManager defaultManager ] createDirectoryAtPath: folderPath withIntermediateDirectories: YES attributes: nil error: NULL ];
        
        NSString *versionNumber = [fileItems objectAtIndex:0];
        [self versionVerify:versionNumber];
    }
    
}

/*
 download other files.
 */
-(void)downLoadOthers:(ASIHTTPRequest*)request{
    
    NSString *folderPath1 = [NSHomeDirectory() stringByAppendingFormat:@"/Library/Application Support/Accounter"];
    countNumber++;
    NSString *path = [folderPath1 stringByAppendingString:[[request url]path]];
    
    NSFileManager *fileManager= [NSFileManager defaultManager]; 
    NSError* err = nil;
    
    [fileManager createDirectoryAtPath:[path stringByDeletingLastPathComponent] withIntermediateDirectories:YES attributes:	nil error:nil];
    
    NSData *data = [NSData dataWithContentsOfURL:[request url]];
    [data writeToFile:path options:NSAtomicWrite error:&err];
    
    [CustomURLProtocol addURL:[mainPath stringByAppendingString:[[request url]absoluteString]]];
    
    NSLog(@"Downloaded:%d",countNumber);
    
    
    if (err) {
        NSLog(@"oops: %@", err);
    }
    
    if(countNumber == 16){
        [self closeView];
    }
    
}

@end
