//
//  AccounterAppDelegate_iPad.h
//  Accounter
//
//  Created by Amrit Mishra on 1/9/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AccounterAppDelegate.h"
#import "Accounter_iPad_native_menuViewController.h"
@interface AccounterAppDelegate_iPad : AccounterAppDelegate {
    
    Accounter_iPad_native_menuViewController *accounteriPadDelegate;
}
@property (nonatomic, retain) IBOutlet Accounter_iPad_native_menuViewController *accounteriPadDelegate;

@end
