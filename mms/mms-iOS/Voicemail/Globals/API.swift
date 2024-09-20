import Foundation
import Alamofire
import SwiftyJSON

public enum HTTPClientResult {
    case success(NSDictionary)
    case failure(String)
}

class API: NSObject {
    
    //    public static let ServerUrl = "http://192.168.0.53/ringfree_voicemail/"
    public static let ServerUrl = "https://slymms.com/"
    public static let BaseUrl: String = "\(ServerUrl)backend"
    
    public static let no_internet_connection_err_msg = "Connection Error"
    
    public static let login = "mobile_login.php"
    public static let register_account = "register_account"
    public static let reset_password = "reset_password"
    public static let get_machine_data = "mobile_get_machine_data.php"
    
    
    public static let verification_account = "verification_account"
    public static let resend = "resend"
    
    public static let getMainData = "getMainData"
    public static let signature = "signature"
    
    public static let getRecordings = "getRecordings"
    public static let addRecording = "addRecording"
    public static let updateRecording = "updateRecording"
    public static let deleteRecording = "deleteRecording"
    
    public static let getVoices = "getVoices"
    public static let addVoice = "addVoice"
    public static let updateVoice = "updateVoice"
    public static let deleteVoice = "deleteVoice"
    
    public static let getScripts = "getScripts"
    public static let addScript = "addScript"
    public static let updateScript = "updateScript"
    public static let deleteScript = "deleteScript"
    
    public static let getPendingOrders = "getPendingOrders"
    public static let getScheduledOrders = "getScheduledOrders"
    public static let getDeliveredOrders = "getDeliveredOrders"
    public static let cancelOrder = "cancelOrder"
    public static let getMailLogs = "getMailLogs"
    
    public static let getContactGroups = "getContactGroups"
    public static let addContactGroup = "addContactGroup"
    public static let updateContactGroup = "updateContactGroup"
    public static let deleteContactGroup = "deleteContactGroup"
    public static let addContact = "addContact"
    public static let addContacts = "addContacts"
    public static let updateContact = "updateContact"
    public static let deleteContact = "deleteContact"
    public static let getContacts = "getContacts"
    
    public static let saveMailLog = "saveMailLog"
    public static let saveTestMailLog = "saveTestMailLog"
    public static let checkExistInOrder = "checkExistInOrder"
    
    
    
    // MARK: - Request Functions
    public static func getUrl(_ api: String) -> URL? {
        return URL(string: "\(BaseUrl)/\(api)")
    }
    
    static func postRequest(api: String, params: Parameters, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            var headers = HTTPHeaders()
            headers["Content-Type"] = "application/json"
            headers["Accept"] = "application/json"
            AF.request(url,
                       method: .post,
                       parameters: params,
                       encoding: JSONEncoding.default,
                       headers: headers)
            .responseData { (response) in
                let statusCode = response.response?.statusCode
                switch statusCode{
                case 200, 201, 204:
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                    break
                case 405:
                    completion(.failure("The parameters were valid but the request failed."))
                    break
                case 400:
                    completion(.failure("The request was unacceptable, often due to missing a required parameter."))
                    break
                case 401:
                    completion(.failure("No valid auth token provided."))
                    break
                case 403:
                    completion(.failure("The API key doesn't have permissions to perform the request."))
                    break
                case 404:
                    completion(.failure("The requested resource doesn't exist."))
                    break
                case 409:
                    completion(.failure("The request conflicts with another request (perhaps due to using the same idempotent key)."))
                    break
                case 429:
                    completion(.failure("Too many requests hit the API too quickly. We recommend an exponential backoff of your requests."))
                    break
                case 500:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                default:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                }
            }
        } else {
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    
    public static func getRequest(api: String, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            //            var headers = HTTPHeaders()
            //            headers["Content-Type"] = "application/json"
            //            headers["Accept"] = "application/json"
            AF.request(url, method: .get/*, headers: headers*/).responseData { (response) in
                let statusCode = response.response?.statusCode
                switch statusCode{
                case 200, 201, 204:
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                    break
                case 405:
                    completion(.failure("The parameters were valid but the request failed."))
                    break
                case 400:
                    completion(.failure("The request was unacceptable, often due to missing a required parameter."))
                    break
                case 401:
                    completion(.failure("No valid auth token provided."))
                    break
                case 403:
                    completion(.failure("The API key doesn't have permissions to perform the request."))
                    break
                case 404:
                    completion(.failure("The requested resource doesn't exist."))
                    break
                case 409:
                    completion(.failure("The request conflicts with another request (perhaps due to using the same idempotent key)."))
                    break
                case 429:
                    completion(.failure("Too many requests hit the API too quickly. We recommend an exponential backoff of your requests."))
                    break
                case 500:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                default:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                }
            }
        } else {
            completion(.failure(no_internet_connection_err_msg))
        }
    }
    
    
    static func postRequestWithAuth(api: String, params: Parameters, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.request(url, method: .post, parameters: params, headers: headers).responseData { (response) in
                let statusCode = response.response?.statusCode
                switch statusCode{
                case 200, 201, 204:
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                    break
                case 405:
                    completion(.failure("The parameters were valid but the request failed."))
                    break
                case 400:
                    completion(.failure("The request was unacceptable, often due to missing a required parameter."))
                    break
                case 401:
                    completion(.failure("No valid auth token provided."))
                    break
                case 403:
                    completion(.failure("The API key doesn't have permissions to perform the request."))
                    break
                case 404:
                    completion(.failure("The requested resource doesn't exist."))
                    break
                case 409:
                    completion(.failure("The request conflicts with another request (perhaps due to using the same idempotent key)."))
                    break
                case 429:
                    completion(.failure("Too many requests hit the API too quickly. We recommend an exponential backoff of your requests."))
                    break
                case 500:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                default:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                }
            }
        } else {
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    public static func getRequestWithAuth(api: String, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.request(url, method: .get, headers: headers).responseData { (response) in
                let statusCode = response.response?.statusCode
                switch statusCode{
                case 200, 201, 204:
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                    break
                case 405:
                    completion(.failure("The parameters were valid but the request failed."))
                    break
                case 400:
                    completion(.failure("The request was unacceptable, often due to missing a required parameter."))
                    break
                case 401:
                    completion(.failure("No valid auth token provided."))
                    break
                case 403:
                    completion(.failure("The API key doesn't have permissions to perform the request."))
                    break
                case 404:
                    completion(.failure("The requested resource doesn't exist."))
                    break
                case 409:
                    completion(.failure("The request conflicts with another request (perhaps due to using the same idempotent key)."))
                    break
                case 429:
                    completion(.failure("Too many requests hit the API too quickly. We recommend an exponential backoff of your requests."))
                    break
                case 500:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                default:
                    completion(.failure("Something went wrong on Stripe's end. (These are rare.)"))
                    break
                }
            }
        } else {
            completion(.failure(no_internet_connection_err_msg))
        }
    }
    
    
    static func putRequest(api: String, params: Parameters, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            //            var headers = HTTPHeaders()
            //            headers["Content-Type"] = "application/json"
            //            headers["Accept"] = "application/json"
            AF.request(url, method: .put, parameters: params/*, headers: headers*/).responseData { (response) in
                let statusCode = response.response?.statusCode
                if statusCode == 200 || statusCode == 204 || statusCode == 201 {
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                } else {
                    completion(.failure(self.no_internet_connection_err_msg))
                }
            }
        } else {
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    static func putRequestWithAuth(api: String, params: Parameters, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.request(url, method: .put, parameters: params, headers: headers).responseData { (response) in
                let statusCode = response.response?.statusCode
                if statusCode == 200 || statusCode == 204 || statusCode == 201 {
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                } else {
                    completion(.failure(self.no_internet_connection_err_msg))
                }
            }
        } else {
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    static func deleteRequest(api: String, params: Parameters, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            //            var headers = HTTPHeaders()
            //            headers["Content-Type"] = "application/json"
            //            headers["Accept"] = "application/json"
            AF.request(url, method: .delete, parameters: params/*, headers: headers*/).responseData { (response) in
                let statusCode = response.response?.statusCode
                if statusCode == 200 || statusCode == 204 || statusCode == 201 {
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                } else {
                    completion(.failure(self.no_internet_connection_err_msg))
                }
            }
        } else {
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    static func deleteRequestWithAuth(api: String, params: Parameters, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork() {
            
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.request(url, method: .delete, parameters: params, encoding: JSONEncoding.default, headers: headers).responseData { (response) in
                let statusCode = response.response?.statusCode
                if statusCode == 200 || statusCode == 204 || statusCode == 201 {
                    switch response.result {
                    case .success(let data):
                        do {
                            if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                                completion(.success(dic))
                            }else{
                                completion(.failure(no_internet_connection_err_msg))
                            }
                        } catch {
                            completion(.failure(no_internet_connection_err_msg))
                        }
                        
                        break
                    case .failure(let error):
                        completion(.failure(error.localizedDescription))
                        break
                    }
                } else {
                    completion(.failure(self.no_internet_connection_err_msg))
                }
            }
        } else {
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    class func uploadMultipart(api: String, _ params: Parameters, _ images: [UIImage], completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork(){
            //            var headers = HTTPHeaders()
            //            headers["Content-Type"] = "application/json"
            //            headers["Accept"] = "application/json"
            AF.upload(multipartFormData: { multipartFormData in
                let count = images.count
                if count > 0 {
                    for i in 0 ... count - 1 {
                        let image = images[i]
                        let imgData = image.jpegData(compressionQuality: 0.7)!
                        let name = "upload_file[]"
                        multipartFormData.append(imgData, withName: name, fileName: "\(name).jpg", mimeType: "image/jpg")
                    }
                }
                for (key, value) in params {
                    multipartFormData.append("\(value)".data(using: String.Encoding.utf8)!, withName: key as String)
                }
            }, to: url)
            .responseData { (response) in
                switch response.result {
                case .success(let data):
                    do {
                        if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                            completion(.success(dic))
                        }else{
                            completion(.failure(no_internet_connection_err_msg))
                        }
                    } catch {
                        completion(.failure(no_internet_connection_err_msg))
                    }
                    
                    break
                case .failure(let error):
                    completion(.failure(error.localizedDescription))
                    break
                }
            }
        }else{
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    class func uploadImageWithAuth(api: String, _ params: Parameters, _ image: UIImage, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork(){
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.upload(multipartFormData: { multipartFormData in
                
                let imgData = image.jpegData(compressionQuality: 0.7)!
                multipartFormData.append(imgData, withName: "sub_media", fileName: "image.jpg", mimeType: "image/jpg")
                
                for (key, value) in params {
                    multipartFormData.append("\(value)".data(using: String.Encoding.utf8)!, withName: key as String)
                }
                
            }, to: url, headers: headers)
            .responseData { (response) in
                switch response.result {
                case .success(let data):
                    do {
                        if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                            completion(.success(dic))
                        }else{
                            completion(.failure(no_internet_connection_err_msg))
                        }
                    } catch {
                        completion(.failure(no_internet_connection_err_msg))
                    }
                    
                    break
                case .failure(let error):
                    completion(.failure(error.localizedDescription))
                    break
                }
            }
        }else{
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    class func uploadProfileWithAuth(api: String, _ params: Parameters, _ profile_image: UIImage, _ user_cover: UIImage, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork(){
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.upload(multipartFormData: { multipartFormData in
                
                let imgData1 = profile_image.jpegData(compressionQuality: 0.7)!
                multipartFormData.append(imgData1, withName: "new_image_url", fileName: "profile_image.jpg", mimeType: "image/jpg")
                
                let imgData2 = user_cover.jpegData(compressionQuality: 0.7)!
                multipartFormData.append(imgData2, withName: "new_image_url_bg", fileName: "user_cover.jpg", mimeType: "image/jpg")
                
                for (key, value) in params {
                    multipartFormData.append("\(value)".data(using: String.Encoding.utf8)!, withName: key as String)
                }
                
            }, to: url, method: .put, headers: headers)
            .responseData { (response) in
                switch response.result {
                case .success(let data):
                    do {
                        if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                            completion(.success(dic))
                        }else{
                            completion(.failure(no_internet_connection_err_msg))
                        }
                    } catch {
                        completion(.failure(no_internet_connection_err_msg))
                    }
                    
                    break
                case .failure(let error):
                    completion(.failure(error.localizedDescription))
                    break
                }
            }
        }else{
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    
    
    class func uploadVideoWithAuth(api: String, _ params: Parameters, _ video_url: URL, completion: @escaping (HTTPClientResult)->Void) {
        if let url = getUrl(api), isConnectedNetwork(){
            let headers: HTTPHeaders = ["Authorization" : "Bearer \(userDefault.string(forKey: C.TOKEN) ?? "")"]
            AF.upload(multipartFormData: { multipartFormData in
                
                if let videoData = try? Data(contentsOf: video_url){
                    multipartFormData.append(videoData, withName: "sub_media", fileName: "video.mp4", mimeType: "video/mp4")
                }
                
                for (key, value) in params {
                    multipartFormData.append("\(value)".data(using: String.Encoding.utf8)!, withName: key as String)
                }
                
            }, to: url, headers: headers)
            .responseData { (response) in
                switch response.result {
                case .success(let data):
                    do {
                        if let dic =  try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary{
                            completion(.success(dic))
                        }else{
                            completion(.failure(no_internet_connection_err_msg))
                        }
                    } catch {
                        completion(.failure(no_internet_connection_err_msg))
                    }
                    
                    break
                case .failure(let error):
                    completion(.failure(error.localizedDescription))
                    break
                }
            }
        }else{
            completion(.failure(self.no_internet_connection_err_msg))
        }
    }
    
    static func isConnectedNetwork() -> Bool {
        return NetworkReachabilityManager()!.isReachable
    }
}


