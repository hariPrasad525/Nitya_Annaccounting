//
//  ResultJSONDeserializer.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "Result.h"
#import "CommandList.h"
#import "Command.h"
#import "ResultList.h"
#import "Cell.h"
#import "Record.h"
#import "InputType.h"

@interface ResultJSONDeserializer : NSObject {
    
    id jsonObject;
}

-(void)setObject :(id)jsonObject;
-(Result*)newDeserializer;

@end
