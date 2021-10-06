//
//  Command.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Command.h"


@implementation Command

- (void)setCode:(NSString *)stringCode{
    code = stringCode;
}

- (NSString *)getCode{
    return code;
}

- (void)setName:(NSString *)stringName{
    name = stringName;
}

- (NSString *)getName{
    return name;
}

@end
