import UIKit

class MFactory: NSObject{

    var id = ""
    var factory_id = ""
    var logo = ""
    var name = ""
    var created_at = ""
    var timezone = 0
    var timezone_name = ""
    var emails = ""
    var report_days = ""
    var shift1_start = ""
    var shift1_end = ""
    var shift2_start = ""
    var shift2_end = ""
    var shift3_start = ""
    var shift3_end = ""
    var groupInfo = [MGroup]()
    
    override init() {
        super.init()
    }
    
    init(dict: NSDictionary) {
        id = dict.parseString(param: "id")
        factory_id = dict.parseString(param: "factory_id")
        logo = dict.parseString(param: "logo")
        name = dict.parseString(param: "name")
        created_at = dict.parseString(param: "created_at")
        timezone = dict.parseInt(param: "timezone")
        timezone_name = dict.parseString(param: "timezone_name")
        emails = dict.parseString(param: "emails")
        report_days = dict.parseString(param: "report_days")
        shift1_start = dict.parseString(param: "shift1_start")
        shift1_end = dict.parseString(param: "shift1_end")
        shift2_start = dict.parseString(param: "shift2_start")
        shift2_end = dict.parseString(param: "shift2_end")
        shift3_start = dict.parseString(param: "shift3_start")
        shift3_end = dict.parseString(param: "shift3_end")
        
        if let arryGroup = dict["groupInfo"] as? [NSDictionary]{
            for item in arryGroup{
                self.groupInfo.append(MGroup.init(dict: item))
            }
        }
    }
}
