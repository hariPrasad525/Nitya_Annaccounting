//
//  Cell.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Cell : NSObject {
    
    NSString *name;
    NSString *value;
    
}

- (void)setName:(NSString *)stringName;
- (void)setValue:(NSString *)stringValue;
- (NSString *)getValue;
- (NSString *)getName;


@end
