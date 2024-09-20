import UIKit
import Alamofire

class SplashVC: BaseVC {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if userDefault.bool(forKey: C.IS_LOGINED){
            self.apiCallForLogin()
        }else{
            GF.goLoginVC()
        }
    }
}

//***************************************//
//         Mark - API Call               //
//***************************************//
extension SplashVC{
    func apiCallForLogin() {
        let factoryID = userDefault.string(forKey: C.FACTORY_ID) ?? ""
        let name = userDefault.string(forKey: C.NAME) ?? ""
        let pass = userDefault.string(forKey: C.PASS) ?? ""
        
        let params: Parameters = ["customer_id": "\(factoryID)",
                                  "username": "\(name)",
                                  "password": "\(pass)"]
//        GF.showLoading()
        API.postRequest(api: API.login, params: params, completion: { result in
//            GF.hideLoading()
            switch result {
            case .success(let dict):
                if dict.parseBool(param: "status"), let dataDic = dict["data"] as? NSDictionary{
                    if let userDic = dataDic["user"] as? NSDictionary{
                        GV.user = MUser.init(dict: userDic)
                    }
                    if let factoryDic = dataDic["factory"] as? NSDictionary{
                        GV.factory = MFactory.init(dict: factoryDic)
                    }
                    DispatchQueue.main.async {
                        GF.goMainVC()
                    }
                }else{
                    DispatchQueue.main.async {
                        GF.goLoginVC()
                    }
                }
            case .failure(let err_msg):
                DispatchQueue.main.async {
                    GF.goLoginVC()
                }
                break
            }
        })
    }
}
