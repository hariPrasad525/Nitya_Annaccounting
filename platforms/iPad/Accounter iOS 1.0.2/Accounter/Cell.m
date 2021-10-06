//
//  Cell.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Cell.h"


@implementation Cell

- (void)setValue:(NSString *)stringValue{
    value = stringValue;
}

- (NSString *)getValue{
    return value;
}

- (void)setName:(NSString *)stringName{
    name = stringName;
}

- (NSString *)getName{
    return name;
}


@end
