//
//  AccounterAppDelegate_iPhone.h
//  Accounter
//
//  Created by Amrit Mishra on 1/9/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AccounterAppDelegate.h"
#import "Accounter_iOS2ViewController.h"

@interface AccounterAppDelegate_iPhone : AccounterAppDelegate {
    
    Accounter_iOS2ViewController *_iPhoneViewController;
}
@property (nonatomic, retain) IBOutlet Accounter_iOS2ViewController *_iPhoneViewController;

@end
