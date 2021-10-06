//
//  FlipsideViewController.h
//  FlipTest
//
//  Created by Amrit Mishra on 9/17/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol HelpViewControllerDelegate;

@interface HelpViewController : UIViewController<UIWebViewDelegate> {
    

    IBOutlet UISegmentedControl *segmentedButton;
    IBOutlet UIView *mainView;
    
    UIWebView *webPage;
}

-(void)addAboutView;
-(void)addHelpView;
-(void)addBlogView;

- (IBAction)changeView:(id)sender;



@property (nonatomic, assign) id <HelpViewControllerDelegate> delegate;

- (IBAction)done:(id)sender;

@end


@protocol HelpViewControllerDelegate
- (void)helpViewControllerDidFinish:(HelpViewController *)controller;
@end
