//
//  SettingsVC.swift
//  Voicemail
//
//  Created by XiaoYan on 7/4/24.
//  Copyright © 2024 newhomepage. All rights reserved.
//

import UIKit

class SettingsVC: UIViewController {
    
    @IBOutlet weak var iAvatar: UIImageView!
    @IBOutlet weak var lName: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        iAvatar.setBorder(color: .lightGray, width: 1)
        iAvatar.sd_setImage(with: URL(string: GV.user.user_picture), placeholderImage: UIImage(named: "ic_avatar")!)
        lName.text = "\(GV.user.username_full)"
    }
    
    @IBAction func bLogoutTapped(_ sender: UIButton) {
        GF.showAlertViewWithTitle("", message: "Are you sure want to logout?", buttonTitles: ["Cancel", "Ok"], viewController: self, completion: {index in
            if index == 1{
                userDefault.set(false, forKey: C.IS_LOGINED)
                userDefault.set("", forKey: C.FACTORY_ID)
                userDefault.set("", forKey: C.NAME)
                userDefault.set("", forKey: C.PASS)
                GF.goLoginVC()
            }
        })
    }

}
