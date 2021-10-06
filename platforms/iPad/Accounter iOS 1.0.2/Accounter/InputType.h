//
//  InputType.h
//  Accounter
//
//  Created by Amrit Mishra on 11/21/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface InputType : NSObject {
    
    int type;
    NSString* name;
    NSString *textValue;
    
}

-(void)setInputType:(int)newType;
-(int)getInputType;
-(UIKeyboardType)getKeyboardType;

-(void)setName:(NSString*)newName;
-(NSString*)getName;


-(void)setTextValue:(NSString*)newTextValue;
-(NSString*)getTextValue;

@end
