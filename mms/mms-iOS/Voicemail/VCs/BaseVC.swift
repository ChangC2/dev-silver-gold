//
//  LightStatusBarVC.swift
//  Sagor
//
//  Created by SilverGold on 2/18/21.
//  Copyright © 2021 Sagor. All rights reserved.
//

import UIKit

class BaseVC: UIViewController {
    
    lazy var bottomSheetTransitioningDelegate = BottomSheetTransitioningDelegate(
        contentHeights: [UIScreen.main.bounds.size.height - 300, UIScreen.main.bounds.size.height - 100],
        presentationDelegate: self
    )

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override var preferredStatusBarStyle : UIStatusBarStyle {
        return GV.darkMode ? .lightContent : .default
    }
}

extension BaseVC: BottomSheetPresentationControllerDelegate {
    func bottomSheetPresentationController(
        _ controller: UIPresentationController,
        shouldDismissBy action: BottomSheetView.DismissAction
    ) -> Bool {
        return true
    }

    func bottomSheetPresentationController(
        _ controller: UIPresentationController,
        didCancelDismissBy action: BottomSheetView.DismissAction
    ) {
        print("Did cancel dismiss by \(action)")
    }

    func bottomSheetPresentationController(
        _ controller: UIPresentationController,
        willDismissBy action: BottomSheetView.DismissAction?
    ) {
        print("Will dismiss dismiss by \(String(describing: action))")
    }

    func bottomSheetPresentationController(
        _ controller: UIPresentationController,
        didDismissBy action: BottomSheetView.DismissAction?
    ) {
        print("Did dismiss dismiss by \(String(describing: action))")
    }
}
