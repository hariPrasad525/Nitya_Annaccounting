//
//  CommandList.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface CommandList : NSObject {

    NSMutableArray *commandNames;
    
}

- (void)setCommadNames:(NSMutableArray *)commandNamesList;
- (NSMutableArray *)getCommandNames;

@end
