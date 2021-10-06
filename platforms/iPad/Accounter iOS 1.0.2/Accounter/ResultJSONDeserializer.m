//
//  ResultJSONDeserializer.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ResultJSONDeserializer.h"


@implementation ResultJSONDeserializer



-(id)init{
    
    self = [super init];
    if (self) {
        
        
    }
    return self;
}

-(void)setObject :(id)newJSONObject{
    
    
    jsonObject = newJSONObject;

}


-(Result*)newDeserializer{
    
    Result *result = [[Result alloc]init];
    
    NSDictionary *jsonObjectDict = [[NSDictionary alloc]init];
    jsonObjectDict = jsonObject;
    
    if([jsonObjectDict valueForKey:@"cookie"]!=nil)
    {
        NSString *cookieString = [[NSString alloc]initWithString:[jsonObject valueForKey:@"cookie"]];
        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        [prefs setObject:cookieString forKey:@"accounterCookie"];
        
        [cookieString release];
        
    }
    
    BOOL value = [[jsonObjectDict valueForKey:@"hideCancel"]boolValue];
    if(value == false)
    {
        [result setHideCancel:false];
    }else{
        [result setHideCancel:true];
    }
    
    BOOL value1 = [[jsonObjectDict valueForKey:@"showBack"]boolValue];
    if(value1 == false)
    {
        [result setShowBack:true];
    }else{
        [result setShowBack:false];
    }
    
    
    if([jsonObjectDict valueForKey:@"title"]!=nil)
    {
        [result setTitle:[jsonObjectDict valueForKey:@"title"]];
    }
    
    
    NSMutableArray *jresultParts = [jsonObjectDict valueForKey:@"resultParts"];
    NSMutableArray * resultParts = [[NSMutableArray alloc]init];
    resultParts = [result getResultParts];
    
    for(int i = 0;i<[jresultParts count];i++){
        id asJsonObject = [jresultParts objectAtIndex:i];
        
        int type = [[asJsonObject valueForKey:@"type"]intValue];
        
        switch (type) {
            case 0:{
                NSString *asString = [[NSString alloc]initWithString:[asJsonObject valueForKey:@"message"]];
                [resultParts addObject:asString];
            }break;
                
            case 1:{
                ResultList *list = [[ResultList alloc]init];
                
                BOOL value = [[asJsonObject valueForKey:@"isMultiSelection"]boolValue];
                [list setMutiSelection:value];
                
                [list setTitle:[asJsonObject valueForKey:@"title"]];
                
                if ([asJsonObject valueForKey:@"isMultiSelection"] == 0) {
                    [list setMutiSelection:FALSE];
                }else{
                    [list setMutiSelection:TRUE];
                }
                [list setName:[asJsonObject valueForKey:@"name"]];
                
                NSMutableArray *records = [[NSMutableArray alloc]init ];
                records =[list getRecords];
                NSMutableArray *asJsonArray = [asJsonObject valueForKey:@"records"];
                
                for (int k =0; k <[asJsonArray count]; k++) {
                    
                    NSDictionary *asJsonObject2 = [[NSDictionary alloc]init];
                    asJsonObject2 = [asJsonArray objectAtIndex:k];
                    
                    
                    Record *record = [[Record alloc]init];
                    [record setCode:[asJsonObject2 valueForKey:@"code"]];
                    
                    NSArray *asJsonArray2 = [asJsonObject2 valueForKey:@"cells"];
                    NSMutableArray *cells = [[NSMutableArray alloc]init];
                    
                    for (int z =0; z < [asJsonArray2 count]; z++) {
                        
                        NSDictionary *asJsonObject3 = [[NSDictionary alloc]init];
                        asJsonObject3  = [asJsonArray2 objectAtIndex:z];
                        
                        Cell *cell = [[Cell alloc]init];
                        [cell setName:[asJsonObject3 valueForKey:@"title"]];
                        [cell setValue:[asJsonObject3 valueForKey:@"value"]];
                        
                        [cells addObject:cell];
                        
                    }
                    [record setCells:cells];
                    [records addObject:record];
                }
                [list setRecords:records];
                [resultParts addObject:list];
                
            }break;
                
            case 2:{
                
                NSMutableArray *asJsonArray = [asJsonObject valueForKey:@"commandNames"];
                if(asJsonArray != nil){
                    
                    CommandList *commandsList = [[CommandList alloc]init];
                    
                    NSMutableArray *commandNames = [[NSMutableArray alloc]initWithArray:[commandsList getCommandNames]];
                    
                    for (int j =0; j < [asJsonArray count];j++) {
                        
                        NSDictionary *asJsonObject2 = [asJsonArray objectAtIndex:j];
                        
                        Command *command = [[Command alloc]init];
                        [command setName:[asJsonObject2 valueForKey:@"name"]];
                        [command setCode:[asJsonObject2 valueForKey:@"code"]];
                        
                        [commandNames addObject:command];
                    }
                    [commandsList setCommadNames:commandNames];
                    [resultParts addObject:commandsList];
                    // [commandNames release];
                }
                
            }break;
                
            case 3:{
                int input =  [[asJsonObject valueForKey:@"inputType"]intValue];
                
                InputType *inputType = [[InputType alloc]init];
                [inputType setInputType:input];
                
                NSString *name = [[NSString alloc]initWithString:[asJsonObject valueForKey:@"name"]];
                [inputType setName:name];
                
                NSString *value = [[NSString alloc]initWithString:[asJsonObject valueForKey:@"value"]];
                [inputType setTextValue:value];
                
                [resultParts addObject:inputType];
                
            }break;
            default:
                break;
        }
        
    }
    
    
    return  result;
}


/*
 -(Result *)getDeserializedObject{
 
 Result *result = [[Result alloc]init];
 
 NSDictionary *jsonObjectDict = [[NSDictionary alloc]init];
 jsonObjectDict = jsonObject;
 
 
 if([jsonObjectDict valueForKey:@"cookie"]!=nil)
 {
 NSString *cookieString = [[NSString alloc]initWithString:[jsonObject valueForKey:@"cookie"]];
 NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
 [prefs setObject:cookieString forKey:@"accounterCookie"];
 
 }
 
 
 NSMutableArray *jresultParts = [jsonObjectDict valueForKey:@"resultParts"];
 
 NSMutableArray * resultParts = [[NSMutableArray alloc]init];
 resultParts = [result getResultParts];
 
 
 for(int i = 0;i<[jresultParts count];i++){
 
 id jsonElement = [jresultParts objectAtIndex:i];
 
 if ([[jsonElement class] isSubclassOfClass:[NSString class]]) {
 NSString *asString = [[NSString alloc]init];
 asString = jsonElement;
 [resultParts addObject:asString];
 }else{
 NSDictionary *asJsonObject = [[NSDictionary alloc]init];
 asJsonObject  = jsonElement;
 
 NSMutableArray *asJsonArray = [asJsonObject valueForKey:@"commandNames"];
 if(asJsonArray != nil){
 
 CommandList *commandsList = [[CommandList alloc]init];
 
 NSMutableArray *commandNames = [[NSMutableArray alloc]initWithArray:[commandsList getCommandNames]];
 
 for (int j =0; j < [asJsonArray count];j++) {
 
 NSDictionary *asJsonObject2 = [asJsonArray objectAtIndex:j];
 
 Command *command = [[Command alloc]init];
 [command setName:[asJsonObject2 valueForKey:@"name"]];
 [command setCode:[asJsonObject2 valueForKey:@"code"]];
 
 [commandNames addObject:command];
 }
 [commandsList setCommadNames:commandNames];
 [resultParts addObject:commandsList];
 
 }else{
 
 ResultList *list = [[ResultList alloc]init];
 
 if ([asJsonObject valueForKey:@"isMultiSelection"] == 0) {
 [list setMutiSelection:FALSE];
 }else{
 [list setMutiSelection:TRUE];
 }
 [list setName:[asJsonObject valueForKey:@"name"]];
 
 NSMutableArray *records = [[NSMutableArray alloc]init ];
 records =[list getRecords];
 NSMutableArray *asJsonArray = [asJsonObject valueForKey:@"records"];
 
 for (int k =0; k <[asJsonArray count]; k++) {
 
 NSDictionary *asJsonObject2 = [[NSDictionary alloc]init];
 asJsonObject2 = [asJsonArray objectAtIndex:k];
 
 
 Record *record = [[Record alloc]init];
 [record setCode:[asJsonObject2 valueForKey:@"code"]];
 
 NSArray *asJsonArray2 = [asJsonObject2 valueForKey:@"cells"];
 NSMutableArray *cells = [[NSMutableArray alloc]init];
 
 for (int z =0; z < [asJsonArray2 count]; z++) {
 
 NSDictionary *asJsonObject3 = [[NSDictionary alloc]init];
 asJsonObject3  = [asJsonArray2 objectAtIndex:z];
 
 Cell *cell = [[Cell alloc]init];
 [cell setName:[asJsonObject3 valueForKey:@"name"]];
 [cell setValue:[asJsonObject3 valueForKey:@"value"]];
 
 [cells addObject:cell];
 
 }
 [record setCells:cells];
 [records addObject:record];
 }
 [list setRecords:records];
 [resultParts addObject:list];
 }
 }
 
 
 }
 if([jsonObjectDict valueForKey:@"inputType"]!=nil)
 {
 NSString *value = [jsonObjectDict valueForKey:@"inputType"];
 [result setInputType:[value intValue]];        
 }
 
 if([jsonObjectDict valueForKey:@"hideCancel"]!=nil)
 {
 
 BOOL value = [[jsonObjectDict valueForKey:@"hideCancel"]boolValue];
 if(value == false)
 {
 [result setHideCancel:false];
 }else{
 [result setHideCancel:true];
 }
 }
 
 if([jsonObjectDict valueForKey:@"showBack"]!=nil)
 {
 BOOL value = [[jsonObjectDict valueForKey:@"showBack"]boolValue];
 if(value == false)
 {
 [result setShowBack:true];
 }else{
 [result setShowBack:false];
 }
 }
 
 if([jsonObjectDict valueForKey:@"title"]!=nil)
 {
 [result setTitle:[jsonObjectDict valueForKey:@"title"]];
 }
 [result setResultParts:resultParts];
 return  result;
 }
 */
@end
