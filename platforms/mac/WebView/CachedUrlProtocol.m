//
//  ALUrlRequest.m
//  Accounter
//
//  Created by Amrit Mishra on 7/19/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "CachedURLProtocol.h"
#import "Foundation/NSCoder.h"


@implementation ConnectionInProgress

@synthesize connection,request,data,response;

@end


@implementation CachedURLProtocol

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
		[NSURLProtocol registerClass:[CachedURLProtocol class]];
        
        
        NSString *folderPath = [NSHomeDirectory() stringByAppendingFormat:@"/Library/Application Support/Accounter/"];
        [ [ NSFileManager defaultManager ] createDirectoryAtPath: folderPath withIntermediateDirectories: YES attributes: nil error: NULL ];
        
        [NSURLCache setSharedURLCache:[[NSURLCache alloc ]initWithMemoryCapacity:1024*1024*4 diskCapacity:1024*1024*50 diskPath:folderPath]];
        
        inited = YES;
        
	}
}



+ (BOOL) canInitWithRequest:(NSURLRequest*)request
 {
     NSString* value=[request valueForHTTPHeaderField:@"OurKey"];
     
     if ([[request HTTPMethod] isEqualToString:@"POST"]) {
         return NO;
     }
     NSString* scheme=[[request URL] absoluteString];
     if (![scheme hasPrefix:@"http://"] && ![scheme hasPrefix:@"https://"]) {
         return NO;
     }
     
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
    }

    id<NSURLProtocolClient> client = [self client];

    
    NSURLRequest *request = [self request];
    
    NSMutableURLRequest *ourRequest = [request mutableCopy];
	[ourRequest setValue:@"Accounter.live.in" forHTTPHeaderField:@"OurKey"]; 
    
    NSURLCache *sharedCache=[NSURLCache sharedURLCache];
    NSCachedURLResponse *urlResponse =  [sharedCache cachedResponseForRequest:request];
    
    if(urlResponse == NULL){
        NSLog(@"request");
       [self readFromRequest:ourRequest];
    }else{
        NSLog(@"cache %@",[[request URL]description]);
        NSURLResponse *cachedResp= [urlResponse response];
//        bool isExpired=false;
//         //NSHTTPURLResponse *res = (NSHTTPURLResponse*)cachedResp;
//       
//            NSDate * _expDate=[self getExpireDate:cachedResp];
//            if ([_expDate compare:[NSDate date]]==NSOrderedAscending) { 
//                isExpired=true;
//            }
//        if (!isExpired) { 
          //    NSLog(@"cache edate: %@ ctime : %@ URL: %@",_expDate,[NSDate date],[request URL]);
            [client URLProtocol:self didReceiveResponse:cachedResp cacheStoragePolicy:NSURLCacheStorageAllowed];
            NSData *data = [urlResponse data];
            [client URLProtocol:self didLoadData:data];
            [client URLProtocolDidFinishLoading:self];
            
//        }else{
//             NSLog(@"cache deleted edate: %@ ctime : %@ URL: %@",_expDate,[NSDate date],[request URL]);
//            [sharedCache removeCachedResponseForRequest:request];
//            NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithDictionary:[ourRequest allHTTPHeaderFields]];
//            [dict removeObjectForKey:@"If-Modified-Since"];
//            [ourRequest setAllHTTPHeaderFields:dict];
//            [self readFromRequest:ourRequest];
//        }

    }
    [ourRequest release];
}


- (void) connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response{
    /* get a reference to the client so we can hand off the data */
    id<NSURLProtocolClient> client = [self client];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSArray* tokens = [response.URL.relativePath componentsSeparatedByString:@"/"];
    if (tokens==nil) {
         [client URLProtocol:self didReceiveResponse:response cacheStoragePolicy:NSURLCacheStorageNotAllowed];
    }
    NSString* pathWithoutRessourceName=@"";
    for (int i=0; i<[tokens count]-1; i++) {
        pathWithoutRessourceName = [pathWithoutRessourceName stringByAppendingString:[NSString stringWithFormat:@"%@%@", [tokens objectAtIndex:i], @"/"]];
    }
    NSString* absolutePath = [NSString stringWithFormat:@"%@%@", documentsDirectory, pathWithoutRessourceName];
    NSString* absolutePathWithRessourceName = [NSString stringWithFormat:@"%@%@", documentsDirectory, response.URL.relativePath];
    
    NSString* ressourceName = [absolutePathWithRessourceName stringByReplacingOccurrencesOfString:absolutePath withString:@""];
    //we're only caching .png, .js, .cgz, .jgz
    if (
        [ressourceName rangeOfString:@".png"].location!=NSNotFound || 
        [ressourceName rangeOfString:@".js"].location!=NSNotFound  ||
        [ressourceName rangeOfString:@".cgz"].location!=NSNotFound || 
        [ressourceName rangeOfString:@".jpg"].location!=NSNotFound ||
        [ressourceName rangeOfString:@".css"].location!=NSNotFound || 
        [ressourceName rangeOfString:@".gif"].location!=NSNotFound ||
        [ressourceName rangeOfString:@".html"].location!=NSNotFound||
        [ressourceName rangeOfString:@".jgz"].location!=NSNotFound) {

        NSLog(@"storing : %@",[[response URL]relativeString]);
        [client URLProtocol:self didReceiveResponse:response cacheStoragePolicy:NSURLCacheStorageAllowed];
    }
    else{
        NSLog(@"leaving : %@",[[response URL]relativeString]);
        [client URLProtocol:self didReceiveResponse:response cacheStoragePolicy:NSURLCacheStorageNotAllowed];
    }

//        NSDate * _expDate=[self getExpireDate:response];
//        NSLog(@"request expire date: %@ current time : %@, URl : %@",_expDate,[NSDate date],[response URL]);
//    if ([_expDate compare:[NSDate date]]==NSOrderedAscending) {  
//            policy=NSURLCacheStorageNotAllowed;
//        }

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
    ConnectionInProgress* progress=[self getProgressForConnection:connection];
    [connections removeObject:progress];

}

- (ConnectionInProgress *) getProgressForConnection:(NSURLConnection *)connection{
    NSLog(@"connection in preogress");
    for (ConnectionInProgress *progress in connections) {
        if (progress.connection==connection) {
            return progress;
        }
    }
    return nil;
}

-(NSDate *)getExpireDate:(NSURLResponse *)responseGot{
    
    NSHTTPURLResponse* httpResponse=(NSHTTPURLResponse*)responseGot;
    
    NSDictionary *headers = [httpResponse allHeaderFields];  
    
    [NSDateFormatter setDefaultFormatterBehavior:NSDateFormatterBehavior10_4];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"EEE,dd MMM yyyy HH:mm:ss zzz"];
    NSString *s1 = [headers valueForKey:@"Expires"];
    
    NSString *dateStr = [headers valueForKey:@"Date"];
    NSDate *pageDate = [dateFormatter dateFromString:dateStr];
    
    NSDate *expireDate = [[NSDate alloc]init];
    if(s1 != nil){
        expireDate = [dateFormatter dateFromString:s1];
    }else{
       // NSLog(@"NO Expired Date");
    }
    
    NSString *_cacheControl = [headers valueForKey:@"Cache-Control"];
    if(_cacheControl != nil){
        NSArray *listItems = [_cacheControl componentsSeparatedByString:@"="];
        if([listItems count]>1){
            NSString *cacheAge = [listItems objectAtIndex:1];
            NSArray *listItems1 = [cacheAge componentsSeparatedByString:@","];
            if([listItems1 count]>0){
                NSString *cacheAgeNumber = [listItems1 objectAtIndex:0];
                long value = [cacheAgeNumber longLongValue];  
                
                expireDate=[[[NSDate date] initWithTimeInterval:value sinceDate:pageDate]autorelease];
                NSLog(@"value : %ld, date : %@",value,expireDate);
            }
        }
    }
    
    [dateFormatter release];
    return expireDate;

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
