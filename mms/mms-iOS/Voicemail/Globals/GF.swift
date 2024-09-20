//
//  GF.swift
//  seemesave
//
//  Created by XiaoYan on 6/9/22.
//

import Foundation
import UIKit
import KRProgressHUD
import Toast_Swift
import Alamofire
import ContactsUI

class GF {
    public static func postNotification(name: String, info:[String: String]?){
        NotificationCenter.default.post(name:NSNotification.Name(rawValue: name), object: nil, userInfo: info)
    }
    
    public static func observeNotification(name: String, completion:@escaping (_ userInfo: [AnyHashable : Any]?) -> Void ){
        NotificationCenter.default.addObserver(forName: NSNotification.Name(rawValue: name), object: nil , queue: nil){
            notification in
            completion(notification.userInfo)
        }
    }
    
    public static func openLink(urlString: String){
        guard let url = URL(string: urlString) else { return }
        UIApplication.shared.open(url)
    }
    
    
    public static func showAlertViewWithTitle(_ title:String?, message:String, buttonTitles:[String], viewController:UIViewController, completion: ((_ index: Int) -> Void)?) {
        
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        for buttonTitle in buttonTitles {
            let alertAction = UIAlertAction(title: buttonTitle, style: .default, handler: { (action:UIAlertAction) in
                completion?(buttonTitles.firstIndex(of: buttonTitle)!)
            })
            alertController .addAction(alertAction)
        }
        viewController .present(alertController, animated: true, completion: nil)
    }
    
    public static func showLoading(){
        DispatchQueue.main.async {
            let style =  KRProgressHUDStyle.custom(background: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 0), text: .black, icon: #colorLiteral(red: 0, green: 0, blue: 0, alpha: 1))
            KRProgressHUD.set(style: style)
            KRProgressHUD.set(activityIndicatorViewColors: [#colorLiteral(red: 0, green: 0, blue: 0, alpha: 1), #colorLiteral(red: 0.501960814, green: 0.501960814, blue: 0.501960814, alpha: 1)])
            KRProgressHUD.show()
        }
    }
    
    public static func showMeassge(messge: String){
        KRProgressHUD.showMessage(messge)
    }
    
    public static func showInfo(messge: String){
        KRProgressHUD.showInfo(withMessage: messge)
    }
    
    public static func showSuccess(message: String){
        KRProgressHUD.showSuccess(withMessage: message)
    }
    
    public static func showError(message: String){
        KRProgressHUD.showError(withMessage: message)
    }
    
    public static func showWarning(message: String){
        KRProgressHUD.showWarning(withMessage: message)
    }
    
    public static func hideLoading(){
        DispatchQueue.main.async {
            KRProgressHUD.dismiss()
        }
    }
    // show MB Progress Hud label
    public static func showLoading(txt: String) {
        KRProgressHUD.set(style: .white)
        KRProgressHUD.show(withMessage: txt)
    }
    
    public static func showToast(msg: String){
        DispatchQueue.main.async {
            appDelegate.window!.makeToast(msg)
        }
    }
    
    public static func showToastMissingParam(){
        DispatchQueue.main.async {
            appDelegate.window!.makeToast("Some information is incomplete, please complete all fields.")
        }
    }
    
    public static func showToastNetworkError(){
        DispatchQueue.main.async {
            appDelegate.window!.makeToast("Please check your internet connection and try again.")
        }
    }
    
    public static func setUserId(userId: String) {
        userDefault.set(userId, forKey: "user_id")
    }
    
    public static func getUserId() -> String {
        return userDefault.string(forKey: "user_id") ?? "0"
    }
    
    public static func getToken() -> String {
        return userDefault.string(forKey: C.TOKEN) ?? ""
    }
    
    
    public static func goLoginVC() {
        guard let window = appDelegate.window else {
            return
        }
        let rootController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "LoginNav")
        window.rootViewController = rootController
        UIView.transition(with: window,
                          duration: 0.5,
                          options: [.transitionCrossDissolve],
                          animations: nil,
                          completion: nil)
    }
    
    public static func goRegisterVC() {
        guard let window = appDelegate.window else {
            return
        }
        let rootController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "SignUpNav")
        window.rootViewController = rootController
        UIView.transition(with: window,
                          duration: 0.5,
                          options: [.transitionCrossDissolve],
                          animations: nil,
                          completion: nil)
    }
    
    public static func goMainVC() {
        guard let window = appDelegate.window else {
            return
        }
        let rootController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MainNav")
        window.rootViewController = rootController
        UIView.transition(with: window,
                          duration: 0.5,
                          options: [.transitionCrossDissolve],
                          animations: nil,
                          completion: nil)
    }
    
    
    public static func getContacts () -> [CNContact]{
        let contactStore = CNContactStore()
        let keysToFetch = [
            CNContactFormatter.descriptorForRequiredKeys(for: .fullName),
            CNContactPhoneNumbersKey,
            CNContactEmailAddressesKey,
            CNContactThumbnailImageDataKey] as [Any]
        
        var allContainers: [CNContainer] = []
        do {
            allContainers = try contactStore.containers(matching: nil)
        } catch {
            print("Error fetching containers")
        }
        
        var results: [CNContact] = []
        
        for container in allContainers {
            let fetchPredicate = CNContact.predicateForContactsInContainer(withIdentifier: container.identifier)
            do {
                let containerResults = try contactStore.unifiedContacts(matching: fetchPredicate, keysToFetch: keysToFetch as! [CNKeyDescriptor])
                results.append(contentsOf: containerResults)
            } catch {
                print("Error fetching containers")
            }
        }
        return results
    }
    
    public static func encrypt(value: String) -> String {
        let xorConstant: UInt8 = 0x53
        if let data = value.data(using: .utf8) {
            var encryptedBytes = [UInt8](data)
            
            for i in 0..<encryptedBytes.count {
                encryptedBytes[i] ^= xorConstant
            }
            
            let encryptedData = Data(encryptedBytes)
            return encryptedData.base64EncodedString(options: .lineLength64Characters)
        }
        return ""
    }
    
    public static func download(url: URL, toFile file: URL, completion: @escaping (Error?) -> Void) {
        DispatchQueue.global(qos: .background).async {
            do {
                let data = try Data.init(contentsOf: url)
                try data.write(to: file, options: .atomic)
                print("saved at \(file.absoluteString)")
                DispatchQueue.main.async {
                    completion(nil)
                }
            } catch {
                completion(error)
                print("an error happened while downloading or saving the file")
            }
        }
    }
    
    public static func loadData(url: URL, completion: @escaping (String, Error?) -> Void) {
        guard let dirPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first
        else {
            completion("", nil)
            return
        }
        let filePath = dirPath.appendingPathComponent(
            url.lastPathComponent,
            isDirectory: false
        )
        let fileManager = FileManager.default
        if fileManager.fileExists(atPath: filePath.path) {
            completion(filePath.absoluteString, nil)
        } else {
            download(url: url, toFile: filePath) { (error) in
                completion(filePath.absoluteString, error)
            }
        }
    }
    
    public static func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
}
