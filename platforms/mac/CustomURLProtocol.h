//
//  ALUrlRequest.h
//  Accounter
//
//  Created by Amrit Mishra on 7/19/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "WebKit/WebKit.h"
#import <Foundation/Foundation.h>
NSMutableArray *cacheArray;

@interface ConnectionInProgress:NSObject {
    NSURLConnection *connection;
    NSURLRequest *request;
    NSURLResponse *response;
    NSMutableData *data;
}

@property (readwrite,assign) NSURLConnection *connection;
@property (readwrite,assign) NSURLRequest *request;
@property (readwrite,assign) NSURLResponse *response;
@property (readwrite,assign) NSMutableData *data;

@end



@interface CustomURLProtocol : NSURLProtocol {
    
    NSMutableArray *connections;
    
}
+ (NSString*) specialProtocolScheme;
+ (NSString*) specialProtocolVarsKey;
+ (void) registerSpecialProtocol;

+(void)addURL:(NSString*)url;
- (void) connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response;
- (void) connection:(NSURLConnection *)connection didReceiveData:(NSData *)data;
- (void) connectionDidFinishLoading:(NSURLConnection *)connection;
- (ConnectionInProgress *) getProgressForConnection:(NSURLConnection *)connection;
-(NSDate *)getExpireDate:(NSURLResponse*)responseGot;
-(void)readFromRequest:(NSURLRequest *)requestGot;
@end


