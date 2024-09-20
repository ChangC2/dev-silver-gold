import UIKit

class MGantt: NSObject{

    var id = ""
    var created_at = ""
    var time_stamp = ""
    var machine_id = ""
    var Operator = ""
    var status = ""
    var color = ""
    var start = 0
    var end = 0
    var time_stamp_ms = 0
    var job_id = ""
    var comment = ""
    var main_program = ""
    var current_program = ""
    var interface = ""
    
    override init() {
        super.init()
    }
    
    init(dict: NSDictionary) {
        id = dict.parseString(param: "id")
        created_at = dict.parseString(param: "created_at")
        time_stamp = dict.parseString(param: "time_stamp")
        machine_id = dict.parseString(param: "machine_id")
        Operator = dict.parseString(param: "Operator")
        status = dict.parseString(param: "status")
        color = dict.parseString(param: "color")
        start = dict.parseInt(param: "start")
        end = dict.parseInt(param: "end")
        time_stamp_ms = dict.parseInt(param: "time_stamp_ms")
        job_id = dict.parseString(param: "job_id")
        comment = dict.parseString(param: "comment")
        main_program = dict.parseString(param: "main_program")
        current_program = dict.parseString(param: "current_program")
        interface = dict.parseString(param: "interface")
    }
}
