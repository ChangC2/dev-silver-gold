//
//  LoginVC.swift
//  seemesave
//
//  Created by XiaoYan on 6/16/22.
//

import UIKit
import CountryPickerView
import Alamofire

class SignUpVC: BaseVC {
    
    @IBOutlet weak var vSerialKey: UIView!
    @IBOutlet weak var vFactoryID: UIView!
    @IBOutlet weak var vFactoryName: UIView!
    @IBOutlet weak var vUsername: UIView!
    @IBOutlet weak var vFullName: UIView!
    
    @IBOutlet weak var vPass: UIView!
    @IBOutlet var vRePass: UIView!
    
    @IBOutlet weak var tSerialKey: UITextField!
    @IBOutlet weak var tFactoryID: UITextField!
    @IBOutlet weak var tFactoryName: UITextField!
    @IBOutlet weak var tUsername: UITextField!
    @IBOutlet weak var tFullName: UITextField!
    @IBOutlet weak var tPass: UITextField!
    @IBOutlet var tRePass: UITextField!
    
    @IBOutlet weak var bAgree: UIButton!
    

    override func viewDidLoad() {
        super.viewDidLoad()
        initUI()
    }
    
    
    @IBAction func bAgreeTapped(_ sender: UIButton) {
        bAgree.isSelected = !bAgree.isSelected
    }
    
    @IBAction func bTermsTapped(_ sender: UIButton) {
        GF.openLink(urlString: "https://slymms.com/")
    }
   
    @IBAction func bSignUpTapped(_ sender: UIButton) {
        apiCallForRegisterWithEmail()
    }
    
    @IBAction func bSignInTapped(_ sender: UIButton) {
        GF.goLoginVC()
    }
    
    func initUI(){
      
        vSerialKey.setBorder(color: .lightGray, width: 1)
        vFactoryID.setBorder(color: .lightGray, width: 1)
        vFactoryName.setBorder(color: .lightGray, width: 1)
        vUsername.setBorder(color: .lightGray, width: 1)
        vFullName.setBorder(color: .lightGray, width: 1)
        vPass.setBorder(color: .lightGray, width: 1)
        vRePass.setBorder(color: .lightGray, width: 1)

        vSerialKey.setCornerRadius(radius: 10)
        vFactoryID.setCornerRadius(radius: 10)
        vFactoryName.setCornerRadius(radius: 10)
        vUsername.setCornerRadius(radius: 10)
        vFullName.setCornerRadius(radius: 10)
        vPass.setCornerRadius(radius: 10)
        vRePass.setCornerRadius(radius: 10)
    }
    
    
    func goOTPVC(){
        let stb = UIStoryboard(name: "Main", bundle: nil)
        if let vc = stb.instantiateViewController(withIdentifier: "OtpVC") as?  OtpVC{
            navigationController?.pushViewController(vc, animated: true)
        }
    }
}


//***************************************//
//           Mark - Extentions           //
//***************************************//


//***************************************//
//         Mark - API Call               //
//***************************************//
extension SignUpVC{
    
    func apiCallForRegisterWithEmail() {
        if !tSerialKey.isValidEmail || tPass.isEmpty || tUsername.isEmpty{
            GF.showToastMissingParam()
            return
        }
        
        if !tPass.text!.elementsEqual(tRePass.text!){
            GF.showToast(msg: "Mismatch Password")
            return
        }

        let params: Parameters = ["accountname": "\(tSerialKey.text!)",
                                  "email": tUsername.text!,
                                  "password" : tPass.text!]

        GF.showLoading()
        API.postRequest(api: API.register_account, params: params, completion: { result in
            GF.hideLoading()
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"){
                    DispatchQueue.main.async {
                        GF.showAlertViewWithTitle("", message: "We have sent mail to your email address to activate your account", buttonTitles: ["OK"], viewController: self, completion: {result in
                            self.goOTPVC()
                        })
                    }
                }else{
                    GF.showToast(msg: dict.parseString(param: "message"))
                }
            case .failure(let err_msg):
                GF.showToast(msg: err_msg)
                break
            }
        })
    }
}
