//
//  Command.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Command : NSObject {
  
    NSString *name;
    NSString *code;

}

- (NSString *)getCode;
- (NSString *)getName;
- (void)setName:(NSString *)stringName;
- (void)setCode:(NSString *)stringCode;

@end
