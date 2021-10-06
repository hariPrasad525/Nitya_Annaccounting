//
//  Result.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Result : NSObject {
    
    NSMutableArray *resultParts;
    NSString *cookie;
    int inputType;
    NSString *title;
    BOOL hideCancel;
    BOOL showBack;
    
}

- (void)setResultParts:(NSMutableArray *)resultPartsList;
- (NSMutableArray *)getResultParts;

- (void)setCookie:(NSString *)newCookie;
- (NSString *)getCookie;

-(void)setInputType:(int)newInputType;
-(int)getInputType;

-(void)setHideCancel:(BOOL)value;
-(BOOL)hideCancelValue;

-(void)setShowBack:(BOOL)value;
-(BOOL)showBackValue;


- (void)setTitle:(NSString *)newTitle;
- (NSString *)getTitle;


@end
