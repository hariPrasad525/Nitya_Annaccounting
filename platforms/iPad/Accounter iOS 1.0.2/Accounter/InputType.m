//
//  InputType.m
//  Accounter
//
//  Created by Amrit Mishra on 11/21/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "InputType.h"


@implementation InputType

-(id)init{
    
    self = [super init];
    if (self) {
        name = [[NSString alloc]init];
        
        textValue = [[NSString alloc]init];
        
    }
    return self;
}


-(void)setInputType:(int)newType{
    type = newType;
}
-(int)getInputType{

    return type;
}


-(UIKeyboardType)getKeyboardType{
    
    
    UIKeyboardType keyType;
    switch (type) {
        case 1:
            keyType = UIKeyboardTypeDefault;
            break;
        case 2:
            keyType =UIKeyboardTypeNumbersAndPunctuation;
            break;
        case 3:
            keyType = UIKeyboardTypeNumbersAndPunctuation;
            break;
        case 4:
            keyType = UIKeyboardTypeDefault;
            break;
        case 5:
            keyType = UIKeyboardTypeEmailAddress;
            break;
        case 6:
            keyType = UIKeyboardTypeNumbersAndPunctuation;
            break;
        case 7:
            keyType =  UIKeyboardTypeURL;
            break;
        case 8:
            keyType = UIKeyboardTypeDecimalPad;
            break;
        default:
            keyType = UIKeyboardTypeDefault;
            break;
    }
    return keyType;
}


-(void)setName:(NSString*)newName{
    name = newName;
}
-(NSString*)getName{
    return name;
}

-(void)setTextValue:(NSString*)newTextValue{
    textValue = newTextValue;
}

-(NSString*)getTextValue{
    return textValue;
}

@end
