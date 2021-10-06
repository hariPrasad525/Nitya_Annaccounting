//
//  ALUrlRequest.h
//  Accounter
//
//  Created by Amrit Mishra on 7/19/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "WebKit/WebKit.h"
#import <Foundation/Foundation.h>

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


@interface CacheTime:NSObject {
    NSDate * _expireDate;
    NSDate * _date;
}

@property (readwrite,assign)NSDate * _expireDate; 
@property (readwrite,assign)NSDate * _date;

-(BOOL)isExpire;

@end



@interface CachedURLProtocol : NSURLProtocol {

    NSMutableArray *connections;
    
}
+ (NSString*) specialProtocolScheme;
+ (NSString*) specialProtocolVarsKey;
+ (void) registerSpecialProtocol;

- (void) connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response;
- (void) connection:(NSURLConnection *)connection didReceiveData:(NSData *)data;
- (void) connectionDidFinishLoading:(NSURLConnection *)connection;
- (ConnectionInProgress *) getProgressForConnection:(NSURLConnection *)connection;
-(NSDate *)getExpireDate:(NSURLResponse*)responseGot;
-(void)readFromRequest:(NSURLRequest *)requestGot;
@end


