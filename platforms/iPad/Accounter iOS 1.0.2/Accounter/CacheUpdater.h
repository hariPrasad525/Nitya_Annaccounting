//
//  CacheUpdater.h
//  Accounter
//
//  Created by krishnaiah on 4/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"


@interface CacheUpdater : NSObject<ASIHTTPRequestDelegate, NSURLConnectionDataDelegate>
{
 
    NSString *filename;
    NSFileHandle *file;
    NSMutableData *fileData;
    
    NSString *mPath;
    NSArray *fileItems;
    NSString *folderPath;

    int countNumber;
    float progress;
    UIView *mainView;
    UIAlertView *alert;
    bool initialFile;
}
-(void)setView:(UIView*)view;
-(void)initUpdating:(NSString*)urlPath;
-(void)versionVerify:(NSString*)versionNumber;
-(void)downLoadFileFromList:(NSArray*)fileArray:(NSString*)path;

-(void)startUpdating:(NSString*)versionNumber;

-(void)downLoadInitial:(ASIHTTPRequest *)request;
-(void)downLoadOther:(ASIHTTPRequest *)request;

@end
