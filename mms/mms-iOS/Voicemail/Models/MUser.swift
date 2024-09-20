import UIKit

class MUser: NSObject{

    var id = ""
    var username = ""
    var password = ""
    var username_full = ""
    var user_picture = ""
    var security_level = ""
    var customer_id = ""
    var time_format = ""
    var is_started_auto = ""
    var start_time = ""
    
    override init() {
        super.init()
    }
    
    init(dict: NSDictionary) {
        id = dict.parseString(param: "id")
        username = dict.parseString(param: "username")
        password = dict.parseString(param: "password")
        username_full = dict.parseString(param: "username_full")
        user_picture = dict.parseString(param: "user_picture")
        security_level = dict.parseString(param: "security_level")
        customer_id = dict.parseString(param: "customer_id").trimmingCharacters(in: .whitespaces)
        time_format = dict.parseString(param: "time_format")
        is_started_auto = dict.parseString(param: "is_started_auto")
        start_time = dict.parseString(param: "start_time")
    }
}
