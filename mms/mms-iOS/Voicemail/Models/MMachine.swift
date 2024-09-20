import UIKit

class MMachine: NSObject{

    var id = ""
    var machine_id = ""
    var machine_picture_url = ""
    var status = "Offline"
    var color = "#000000"
    var elapsed = 0
    var utilization = 0.0
    var Operator = ""
    var operator_picture_url = ""
    var job_id = ""
    var gantt = [MGantt]()
    var utilizations = [Double]()
    
    override init() {
        super.init()
    }
    
    init(dict: NSDictionary) {
        id = dict.parseString(param: "id")
        machine_id = dict.parseString(param: "machine_id")
        machine_picture_url = dict.parseString(param: "machine_picture_url")
        status = dict.parseString(param: "status")
        color = dict.parseString(param: "color")
        elapsed = dict.parseInt(param: "elapsed")
        utilization = dict.parseDouble(param: "utilization")
        Operator = dict.parseString(param: "Operator")
        operator_picture_url = dict.parseString(param: "operator_picture_url")
        job_id = dict.parseString(param: "job_id")
        gantt.removeAll()
        if let gArray = dict["gantt"] as? [NSDictionary]{
            for item in gArray{
                gantt.append(MGantt.init(dict: item))
            }
        }
        
        if let uArray = dict["utilizations"] as? [NSDictionary]{
            for item in uArray{
                utilizations.append(item.parseDouble(param: "utilization"))
            }
        }
    }
}
