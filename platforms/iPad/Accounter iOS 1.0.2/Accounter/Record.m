//
//  Record.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Record.h"


@implementation Record

-(id)init{
    
    self = [super init];
    if (self) {
        cells = [[NSMutableArray alloc]init];

    }
    return self;
}

- (NSString *)getCode{
    return code;
}

- (NSMutableArray *)getCells{
    return cells;
}

- (void)setCells:(NSMutableArray *)cellNames{
    cells = [[NSMutableArray alloc]initWithArray:cellNames];
}

- (void)setCode:(NSString *)stringCode{
    code = stringCode;
}

@end
