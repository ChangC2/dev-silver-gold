//
//  MainVC.swift
//  seemesave
//
//  Created by XiaoYan on 6/21/22.
//

import UIKit
import CoreLocation
import ContactsUI
import Alamofire

class MainVC: BaseVC {
    
    @IBOutlet weak var iTab1: UIImageView!
    @IBOutlet weak var iTab2: UIImageView!
    @IBOutlet weak var iTab3: UIImageView!
    @IBOutlet weak var iTab4: UIImageView!
    @IBOutlet weak var iTab5: UIImageView!
    
    @IBOutlet weak var vTab1: UIView!
    @IBOutlet weak var vTab2: UIView!
    @IBOutlet weak var vTab3: UIView!
    @IBOutlet weak var vTab4: UIView!
    @IBOutlet weak var vTab5: UIView!
    
    
    @IBOutlet weak var lTab1: UILabel!
    @IBOutlet weak var lTab2: UILabel!
    @IBOutlet weak var lTab3: UILabel!
    @IBOutlet weak var lTab4: UILabel!
    @IBOutlet weak var lTab5: UILabel!
    
    
    @IBOutlet weak var vContent: UIView!
    
    var currentVC: UIViewController?
    
    var vc0: UIViewController?
    var vc1: UIViewController?
    var vc2: UIViewController?
    var vc3: UIViewController?
    var vc4: UIViewController?
    
    var tabImages = [UIImageView]()
    var tabLabels = [UILabel]()
    
    var tabIndex = -1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initUI()
        initGestures()
    }
    
    @objc func handleVTab1Gesture (sender: UITapGestureRecognizer) {
         setTab(index: 0)
    }
    
    @objc func handleVTab2Gesture (sender: UITapGestureRecognizer) {
        setTab(index: 1)
    }
    
    @objc func handleVTab3Gesture (sender: UITapGestureRecognizer) {
        setTab(index: 2)
    }
    
    @objc func handleVTab4Gesture (sender: UITapGestureRecognizer) {
        setTab(index: 3)
    }
    
    @objc func handleVTab5Gesture (sender: UITapGestureRecognizer) {
        setTab(index: 4)
    }
    
    func initUI(){
        tabImages.append(iTab1)
        tabImages.append(iTab2)
        tabImages.append(iTab3)
        tabImages.append(iTab4)
        tabImages.append(iTab5)
        
        tabLabels.append(lTab1)
        tabLabels.append(lTab2)
        tabLabels.append(lTab3)
        tabLabels.append(lTab4)
        tabLabels.append(lTab5)
    
        setTab(index: 0)
    }
    
    func initGestures(){
        let vTab1Gesture = UITapGestureRecognizer(target: self, action: #selector(self.handleVTab1Gesture))
        vTab1.addGestureRecognizer(vTab1Gesture)
        
        let vTab2Gesture = UITapGestureRecognizer(target: self, action: #selector(self.handleVTab2Gesture))
        vTab2.addGestureRecognizer(vTab2Gesture)
        
        let vTab3Gesture = UITapGestureRecognizer(target: self, action: #selector(self.handleVTab3Gesture))
        vTab3.addGestureRecognizer(vTab3Gesture)
        
        let vTab4Gesture = UITapGestureRecognizer(target: self, action: #selector(self.handleVTab4Gesture))
        vTab4.addGestureRecognizer(vTab4Gesture)
        
        let vTab5Gesture = UITapGestureRecognizer(target: self, action: #selector(self.handleVTab5Gesture))
        vTab5.addGestureRecognizer(vTab5Gesture)
    }
    
    func showContentVC() {
        switch tabIndex {
        case 0:
            if vc0 == nil{
                let stb = UIStoryboard(name: "Main", bundle: nil)
                if let vc = stb.instantiateViewController(withIdentifier: "HomeNav") as?  UINavigationController{
                    vc0 = vc
                    addContentVC(vc: vc)
                }
            }else{
                addContentVC(vc: vc0!)
            }
            break
        case 2:
            if vc2 == nil{
                let stb = UIStoryboard(name: "Main", bundle: nil)
                if let vc = stb.instantiateViewController(withIdentifier: "ReportsNav") as?  UINavigationController{
                    vc2 = vc
                    addContentVC(vc: vc)
                }
            }else{
                addContentVC(vc: vc2!)
            }
            break
        case 3:
            if vc3 == nil{
                let stb = UIStoryboard(name: "Main", bundle: nil)
                if let vc = stb.instantiateViewController(withIdentifier: "ReportsNav") as?  UINavigationController{
                    vc3 = vc
                    addContentVC(vc: vc)
                }
            }else{
                addContentVC(vc: vc3!)
            }
            break
        case 4:
            if vc4 == nil{
                let stb = UIStoryboard(name: "Main", bundle: nil)
                if let vc = stb.instantiateViewController(withIdentifier: "SettingsNav") as?  UINavigationController{
                    vc4 = vc
                    addContentVC(vc: vc)
                }
            }else{
                addContentVC(vc: vc4!)
            }
            break
        default:
            break
        }
    }
    
    func setTab(index: Int){
        if tabIndex == index{
            return
        }
        tabIndex = index
        for i in 0..<5{
            tabImages[i].image = tabImages[i].image?.withRenderingMode(.alwaysTemplate)
            tabImages[i].tintColor = i == index ? UIColor(named: "appMainColor") : .lightGray
            tabLabels[i].textColor = i == index ? UIColor(named: "appMainColor") : .lightGray
        }
        showContentVC()
    }
    
    
    func addContentVC(vc: UIViewController){
        addChild(vc)
        vc.view.frame = vContent.bounds
        vc.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        vc.didMove(toParent: self)
        vContent.addSubview(vc.view)
        if currentVC != nil {
            currentVC?.view.removeFromSuperview()
            currentVC?.removeFromParent()
        }
        currentVC = vc
    }
}
