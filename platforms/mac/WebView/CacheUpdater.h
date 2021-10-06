//
//  CacheUpdater.h
//  Accounter
//
//  Created by krishnaiah on 4/9/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"
#import "CustomURLProtocol.h"
#import "ASINetworkQueue.h"

@interface CacheUpdater : NSObject<ASIHTTPRequestDelegate,NSURLConnectionDelegate>{

    NSString *filename;
    NSFileHandle *file;
    NSMutableData *fileData;
    
    NSString *mainPath;
    NSArray *fileItems;
    NSString *folderPath;
    NSWindow *mainWindow;
    NSPanel *viewPanel;
    
    bool initialFile;
    int countNumber;
    
    
    

    
}

-(void)initUpdating:(NSString*)urlPath;
-(void)setMainWindow:(NSWindow*)window:(NSPanel*)panel;
-(void)downLoadFileFromList:(NSArray*)fileArray:(NSString*)path;
-(void)versionVerify:(NSString*)versionNumber;
-(void)createDownLoadView;
-(void)closeView;
-(void)startUpdating:(NSString*)versionNumber;
-(void)downLoadMain:(ASIHTTPRequest*)request;
-(void)downLoadOthers:(ASIHTTPRequest*)request;

-(void)queueCompleted:(ASINetworkQueue*)queue;

@end
