//
//  Result.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Result.h"


@implementation Result


-(id)init{
    
    
    self = [super init];
    if (self) {
        resultParts = [[NSMutableArray alloc]init];
        title = [[NSString alloc]init];
        hideCancel = false;
        showBack = false;
        
    }
    return self;
}

- (void)setResultParts:(NSMutableArray *)resultPartsList{
    resultParts = [[NSMutableArray alloc]initWithArray:resultPartsList];
}

- (NSMutableArray *)getResultParts{
    return resultParts;
}


- (void)setCookie:(NSString *)newCookie{
    
    cookie = [[NSString alloc]initWithString:newCookie];
}
- (NSString *)getCookie{
    return cookie;
}



-(void)setInputType:(int)newInputType{
    inputType = newInputType;
}
-(int)getInputType{
    return inputType;
}

-(void)setHideCancel:(BOOL)value{
    hideCancel = value;
}
-(BOOL)hideCancelValue{
    return hideCancel;
}

-(void)setShowBack:(BOOL)value{
    showBack = value;
}
-(BOOL)showBackValue{
    return showBack;
}


- (void)setTitle:(NSString *)newTitle{
    title = newTitle;
}
- (NSString *)getTitle{
    return title;
}

@end
