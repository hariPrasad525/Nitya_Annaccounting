//
//  CustomURLProtocol.m
//  Accounter
//
//  Created by krishnaiah on 4/9/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CustomURLProtocol.h"

@implementation ConnectionInProgress

@synthesize connection,request,data,response;

@end


@implementation CustomURLProtocol

+ (NSString*) specialProtocolScheme {
	return @"special";
}


+ (NSString*) specialProtocolVarsKey {
	return @"specialVarsKey";
}

+ (BOOL)registerClass:(Class)newProtocolClass
{
    return TRUE;
}

+ (void)unregisterClass:(Class)newProtocolClass
{
    
}


+ (void) registerSpecialProtocol {
	static BOOL inited = NO;
	if ( ! inited ) {
		[NSURLProtocol registerClass:[CustomURLProtocol class]];
        
        

        inited = YES;
        
	}
}



+ (BOOL) canInitWithRequest:(NSURLRequest*)request
{

    if ([[request HTTPMethod] isEqualToString:@"POST"]) {
        return NO;
    }
    NSString* scheme=[[request URL] absoluteString];
    if (![scheme hasPrefix:@"http://"] && ![scheme hasPrefix:@"https://"]) {
        return NO;
    }
    
    if([[[request URL]lastPathComponent]isEqualToString:@"accounter"]){
        return NO;
        
    }
    if([[[request URL]lastPathComponent]isEqualToString:@"newmenu"]){
        return NO;
        
    }
//    if([[[request URL]absoluteString]rangeOfString:@"accounter.client.nocache.js"].length  != 0){
//        return NO;
//
//    }
//    if([[[request URL]absoluteString]rangeOfString:@".cache.html"].length  != 0){
//        return NO;
// 
//    }
//    
    
    NSLog(@"request : %@",[[request URL]absoluteString]);
    NSString* value=[request valueForHTTPHeaderField:@"OurKey"];
    
    
    if(value==Nil){
        return YES;
    }
    return NO;
}


+ (NSURLRequest *)canonicalRequestForRequest:(NSURLRequest *)newRequest
{
    return newRequest;
}

+ (BOOL)requestIsCacheEquivalent:(NSURLRequest *)aRequest toRequest:(NSURLRequest *)bRequest
{
    return TRUE;
}

- (void) startLoading
{
    
  	if(connections==NULL){
        connections  = [[NSMutableArray alloc]init];
    };
    
    id<NSURLProtocolClient> client = [self client];
    
    
    NSURLRequest *request = [self request];
  
    
    NSString *folderPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    
    
    NSString* storagePath = [NSString stringWithFormat:@"%@%@", folderPath, request.URL.relativePath];
    
    NSData* content;
    
    NSCachedURLResponse* cacheResponse = nil;
    if ([[NSFileManager defaultManager] fileExistsAtPath:storagePath]) {
        NSLog(@"CACHE FOUND for %@", request.URL.relativePath);
        content = [[NSData dataWithContentsOfFile:storagePath] retain];
        
        NSURLResponse* response = [[NSURLResponse alloc] initWithURL:request.URL MIMEType:@"" expectedContentLength:[content length] textEncodingName:nil];
        cacheResponse = [[NSCachedURLResponse alloc] initWithResponse:response data:content];
        
    }
        
    
    if(cacheResponse == NULL){
        
        NSMutableURLRequest *ourRequest = [request mutableCopy];
        [ourRequest setValue:@"Accounter.live.in" forHTTPHeaderField:@"OurKey"]; 
        [self readFromRequest:ourRequest];
    }else{
        NSLog(@"cache %@",[[request URL]description]);
        NSURLResponse *cachedResp= [cacheResponse response];
        
        [client URLProtocol:self didReceiveResponse:cachedResp cacheStoragePolicy:NSURLCacheStorageAllowed];
        NSData *data = [cacheResponse data];
        [client URLProtocol:self didLoadData:data];
        [client URLProtocolDidFinishLoading:self];
    }
    
}


- (void) connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response{
    /* get a reference to the client so we can hand off the data */
    id<NSURLProtocolClient> client = [self client];
    

   // NSLog(@"leaving : %@",[[response URL]relativeString]);
    [client URLProtocol:self didReceiveResponse:response cacheStoragePolicy:NSURLCacheStorageNotAllowed];
      
       
}
- (void) connection:(NSURLConnection *)connection didReceiveData:(NSData *)data{
    /* get a reference to the client so we can hand off the data */
    id<NSURLProtocolClient> client = [self client];
    [client URLProtocol:self didLoadData:data];
    
    
}
- (void) connectionDidFinishLoading:(NSURLConnection *)connection{
    /* get a reference to the client so we can hand off the data */
    
    id<NSURLProtocolClient> client = [self client];
    [client URLProtocolDidFinishLoading:self];
    
}
- (void) connection:(NSURLConnection *)connection didFailWithError:(NSError *)error{
    NSLog(@"error");
    id<NSURLProtocolClient> client = [self client];
    [client URLProtocol:self  didFailWithError:error];
//    ConnectionInProgress* progress=[self getProgressForConnection:connection];
//    [connections removeObject:progress];
    
}


-(void)readFromRequest:(NSURLRequest *)requestGot
{
    NSURLConnection *connection=[[NSURLConnection alloc] initWithRequest:requestGot delegate:self];
    [connection release];
}


- (void) stopLoading
{
    
}



@end
