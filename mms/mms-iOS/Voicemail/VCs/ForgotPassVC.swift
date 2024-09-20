//
//  LoginVC.swift
//  seemesave
//
//  Created by XiaoYan on 6/16/22.
//

import UIKit
import CountryPickerView
import Alamofire

class ForgotPassVC: BaseVC {
    
    @IBOutlet weak var vEmailContainer: UIView!
    
    @IBOutlet weak var vEmail: UIView!
    
    @IBOutlet weak var tEmail: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initUI()
    }
  
    
    
    @IBAction func bSignInTapped(_ sender: UIButton) {
        apiCallForResetPassword()
    }
    
    @IBAction func bCancelTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    func initUI(){
        vEmail.setBorder(color: .lightGray, width: 1)
        vEmail.setCornerRadius(radius: 10)
      
        tEmail.text = "mzhou9954@gmail.com"
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
extension ForgotPassVC{
    func apiCallForResetPassword() {
        if !tEmail.isValidEmail{
            GF.showToastMissingParam()
            return
        }
        let params: Parameters = ["email": tEmail.text!]
        GF.showLoading()
        API.postRequest(api: API.reset_password, params: params, completion: { result in
            GF.hideLoading()
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"){
                    DispatchQueue.main.async {
                        GF.showAlertViewWithTitle("", message: "Verification code have been sent to you. Please verify your email address.", buttonTitles: ["OK"], viewController: self, completion: {result in
                            userDefault.set(dict.parseString(param: "token"), forKey: C.TOKEN)
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
