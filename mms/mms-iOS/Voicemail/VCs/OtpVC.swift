//
//  OtpVC.swift
//  Anoki
//
//  Created by XiaoYan on 4/4/23.
//  Copyright © 2023 seller. All rights reserved.
//

import UIKit
import Alamofire

class OtpVC: UIViewController {
    
    @IBOutlet weak var lblCode1: UILabel!
    @IBOutlet weak var lblCode2: UILabel!
    @IBOutlet weak var lblCode3: UILabel!
    @IBOutlet weak var lblCode4: UILabel!
    
    @IBOutlet weak var txtOTP: UITextField!
    @IBOutlet weak var bNext: UIButton!
    
    var codeLabels: [UILabel]!
    var method = "email"
    var email = ""
    var phoneNumber = ""
    var pin = ""
    var password = ""
    var otpId = ""

    override func viewDidLoad() {
        super.viewDidLoad()

        initUI()
    }
    
    @IBAction func bBackTapped(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func bResendTapped(_ sender: UIButton) {
        apiCallForOtpResend()
    }
    
    @IBAction func bNextTapped(_ sender: UIButton) {
        if txtOTP.text!.count < 4{
            GF.showToast(msg: "Please enter valid verification code.")
            return
        }
        apiCallForVerify()
    }
    
    func initUI(){
        txtOTP.delegate = self
        NotificationCenter.default.addObserver(self, selector: #selector(self.textFieldTextDidChangeOneCI(_:)), name: UITextField.textDidChangeNotification, object: self.txtOTP)
        codeLabels = [lblCode1, lblCode2, lblCode3, lblCode4]
        for codeLbl in codeLabels{
            codeLbl.setBorder(color: UIColor(named: "appMiddleGreyColor")!, width: 1)
        }
        enableActive(nCount: 0)
    }
    
    @objc func textFieldTextDidChangeOneCI(_ notification : NSNotification) {
        self.updatePasscodeField()
    }
    
    func updatePasscodeField() {
        let lenght : Int = (self.txtOTP.text?.count)!
        
        var i : Int = 0
        for charater in self.txtOTP.text! {
            for j in i..<4 {
                let label = codeLabels[j] as UILabel
                label.text = ""
            }
            
            let label = codeLabels[i] as UILabel
            label.text = String(charater)
            i = i + 1
            if i > 3 { break }
        }
        
        if lenght == 0 {
            for j in i..<4 {
                let label = codeLabels[j] as UILabel
                label.text = ""
            }
        }
        
        enableActive(nCount: lenght)
    }
    
    func enableActive(nCount: Int) {
        bNext.isUserInteractionEnabled = (nCount < 4) ? false : true
        bNext.alpha = (nCount < 4) ? 0.5 : 1.0
    }
}

extension OtpVC: UITextFieldDelegate {
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let textFieldText = textField.text,
            let rangeOfTextToReplace = Range(range, in: textFieldText) else {
                return false
        }
        let substringToReplace = textFieldText[rangeOfTextToReplace]
        let count = textFieldText.count - substringToReplace.count + string.count
        return count <= 4
    }
}


//***************************************//
//         Mark - API Call               //
//***************************************//
extension OtpVC{
    func apiCallForVerify() {
        var params: Parameters = ["token" : GF.getToken(),
                                  "verification_code": txtOTP.text!]
        GF.showLoading()
        API.postRequestWithAuth(api: API.verification_account, params: params, completion: { result in
            GF.hideLoading()
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"){
                    userDefault.set(true, forKey: C.IS_LOGINED)
                    userDefault.set(dict.parseString(param: "token"), forKey: C.TOKEN)
                    GF.goMainVC()
                }else{
                    GF.showToast(msg: dict.parseString(param: "message"))
                } 
            case .failure(let err_msg):
                GF.showToast(msg: err_msg)
                break
            }
        })
    }
    
    func apiCallForOtpResend() {
        var params: Parameters = ["token" : GF.getToken()]
        GF.showLoading()
        API.postRequestWithAuth(api: API.resend, params: params, completion: { result in
            GF.hideLoading()
            switch result {
            case .success(_):
                GF.showToast(msg: "Verification code have been sent to you again.")
            case .failure(let err_msg):
                GF.showToast(msg: err_msg)
                break
            }
        })
    }
}
