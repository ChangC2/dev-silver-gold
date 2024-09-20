//
//  HomeFilterVC.swift
//  Voicemail
//
//  Created by XiaoYan on 7/6/24.
//  Copyright © 2024 newhomepage. All rights reserved.
//

import UIKit

class HomeFilterVC: UIViewController {
    
    var delegate: SelectDelegate?

    @IBOutlet var vGroup: UIView!
    @IBOutlet var lGroup: UILabel!
    
    var groupIndex = -1
    var groups = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
      
        vGroup.setCornerRadius(radius: 8)
        vGroup.setBorder(color: .lightGray, width: 1)
        
        lGroup.text = userDefault.string(forKey: C.GROUP) ?? "All"
        
        groups.removeAll()
        groups.append("All")
        for g in GV.factory.groupInfo{
            groups.append(g.name)
        }
    }
    
    @IBAction func bSaveTapped(_ sender: UIButton) {
        if groups.count == 0 {
            return
        }
        dismiss(animated: true)
        userDefault.setValue(groups[groupIndex + 1], forKey: C.GROUP)
        delegate?.onSelect(index: groupIndex)
    }
    
    
    @IBAction func bGroupTapped(_ sender: UIButton) {
        let stb = UIStoryboard(name: "Main", bundle: nil)
        if let sVC = stb.instantiateViewController(withIdentifier: "SelectVC") as?  SelectVC{
            sVC.datas = groups
            sVC.delegate = self
            self.present(sVC, animated: true, completion: nil)
        }
    }
    
}

extension HomeFilterVC: SelectDelegate{
    func onSelect(id: String, type: Int) {
        
    }
    
    func onSelect(index: Int) {
        dismiss(animated: true)
        groupIndex = index - 1
        if groupIndex == -1{
            lGroup.text = "All"
        }
        if groupIndex > -1, GV.factory.groupInfo.count > groupIndex{
            lGroup.text = GV.factory.groupInfo[groupIndex].name
        }
    }
}
