//
//  LoginVC.swift
//  seemesave
//
//  Created by XiaoYan on 6/16/22.
//

import UIKit
import CountryPickerView
import Alamofire

class LoginVC: BaseVC {
    
    @IBOutlet var vFactoryId: UIView!
    @IBOutlet var tFactoryId: UITextField!
    @IBOutlet weak var vName: UIView!
    @IBOutlet weak var vPass: UIView!
    @IBOutlet var tName: UITextField!
    @IBOutlet weak var tPass: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initUI()
    }
    
    @IBAction func bForgotPassTapped(_ sender: UIButton) {
        let stb = UIStoryboard(name: "Main", bundle: nil)
        if let vc = stb.instantiateViewController(withIdentifier: "ForgotPassVC") as? ForgotPassVC {
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    @IBAction func bSignInTapped(_ sender: UIButton) {
        apiCallForLogin()
    }
    
    @IBAction func bSignUpTapped(_ sender: UIButton) {
        GF.goRegisterVC()
    }
    
    func initUI(){
        vFactoryId.setBorder(color: .lightGray, width: 1)
        vFactoryId.setCornerRadius(radius: 10)
        
        vName.setBorder(color: .lightGray, width: 1)
        vName.setCornerRadius(radius: 10)
        
        vPass.setBorder(color: .lightGray, width: 1)
        vPass.setCornerRadius(radius: 10)
        
        //        tFactoryId.whitePlaceholder(placeholder: "Factory ID")
        //        tEmail.whitePlaceholder(placeholder: "Username")
        //        tPass.whitePlaceholder(placeholder: "Password")
        tFactoryId.text = "sm_ks"
        tName.text = "cam"
        tPass.text = "2468"
    }
}

//***************************************//
//           Mark - Extentions           //
//***************************************//


//***************************************//
//         Mark - API Call               //
//***************************************//
extension LoginVC{
    func apiCallForLogin() {
        if tFactoryId.isEmpty || tName.isEmpty || tPass.isEmpty{
            GF.showToastMissingParam()
            return
        }
        
        let params: Parameters = ["customer_id": tFactoryId.text!,
                                  "username": tName.text!,
                                  "password": tPass.text!]
        GF.showLoading()
        API.postRequest(api: API.login, params: params, completion: { result in
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"), let dataDic = dict["data"] as? NSDictionary{
                    if let userDic = dataDic["user"] as? NSDictionary{
                        GV.user = MUser.init(dict: userDic)
                    }
                    if let factoryDic = dataDic["factory"] as? NSDictionary{
                        GV.factory = MFactory.init(dict: factoryDic)
                    }
                    userDefault.set(true, forKey: C.IS_LOGINED)
                    userDefault.set(self.tFactoryId.text!, forKey: C.FACTORY_ID)
                    userDefault.set(self.tName.text!, forKey: C.NAME)
                    userDefault.set(self.tPass.text!, forKey: C.PASS)
                    GF.showToast(msg: "Success to login.")
                    GF.goMainVC()
                }else{
                    GF.hideLoading()
                    GF.showToast(msg: dict.parseString(param: "message"))
                }
            case .failure(let err_msg):
                GF.hideLoading()
                GF.showToast(msg: err_msg)
                break
            }
        })
    }
    
}
