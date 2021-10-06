//
//  CommandList.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "CommandList.h"


@implementation CommandList

-(id)init{
    
    self = [super init];
    if (self) {
    commandNames = [[NSMutableArray alloc]init];
    }
     return self;
}

- (void)setCommadNames:(NSMutableArray *)commandNamesList{
    commandNames = [[NSMutableArray alloc]initWithArray:commandNamesList];
}

- (NSMutableArray *)getCommandNames{
    return commandNames;
}


@end
