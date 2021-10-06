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


/*
 this method make helps in deciding the request we need to process. the request which we dont need to save or modify we can by pass then by returning YES.
 and if we need to handle that request return NO. This will call - (void) startLoading method for that request.
 */
+ (BOOL) canInitWithRequest:(NSURLRequest*)request
{
    if ([[request HTTPMethod] isEqualToString:@"POST"]) {
        NSLog(@"not supported : %@",[[request URL]absoluteURL]);
        return NO;
    }
    NSString* scheme=[[request URL] absoluteString];
    
       
    if (![scheme hasPrefix:@"http://"] && ![scheme hasPrefix:@"https://"]) {
        return NO;
    }
    
    if([[[request URL]lastPathComponent]isEqualToString:@"accounter"]){
        return NO;
    }
    
    NSString *folderPath = [NSHomeDirectory() stringByAppendingFormat:@"/Library/Application Support/Accounter/"];
    NSString *relativePath = request.URL.relativePath;
    if([relativePath isEqualToString:@"/"]){
        return NO;
    }
    NSString* storagePath = [NSString stringWithFormat:@"%@%@", folderPath, request.URL.relativePath];
    if ([[NSFileManager defaultManager] fileExistsAtPath:storagePath]) {
        NSLog(@"found : %@",[[request URL]absoluteURL]);
        return YES;
    }else {
        NSLog(@"not found : %@",[[request URL]absoluteURL]);
        return NO;
    }

    return NO;
}



+(void)addURL:(NSString*)url{
    if(cacheArray!=nil){
        cacheArray = [[NSMutableArray alloc]init];
    }
    [cacheArray addObject:url];
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
    }
    
    id<NSURLProtocolClient> client = [self client];
    
    
    NSURLRequest *request = [self request];
    
    
    NSString *folderPath = [NSHomeDirectory() stringByAppendingFormat:@"/Library/Application Support/Accounter/"];
    
    NSString* storagePath = [NSString stringWithFormat:@"%@%@", folderPath, request.URL.relativePath];
    
    NSData* content;
    
    NSCachedURLResponse* cacheResponse = nil;
    if ([[NSFileManager defaultManager] fileExistsAtPath:storagePath]) {
      //  NSLog(@"CACHE FOUND for %@", request.URL.relativePath);
        content = [[NSData dataWithContentsOfFile:storagePath] retain];
        
        NSURLResponse* response = [[NSURLResponse alloc] initWithURL:request.URL MIMEType:@"" expectedContentLength:[content length] textEncodingName:nil];
        cacheResponse = [[NSCachedURLResponse alloc] initWithResponse:response data:content];
        
    }
    
    
   if(cacheResponse == nil){
        NSMutableURLRequest *ourRequest = [request mutableCopy];
        [ourRequest setValue:@"Accounter.live.in" forHTTPHeaderField:@"OurKey"]; 
     
        [self readFromRequest:ourRequest];
        
    }else{
//        NSLog(@"cache %@",[[request URL]description]);
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
