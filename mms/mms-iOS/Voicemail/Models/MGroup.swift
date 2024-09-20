import UIKit

class MGroup: NSObject{

    var id = ""
    var name = ""
    var machine_list = ""
//    var machine_list = [String]()
    
    override init() {
        super.init()
    }
    
    init(dict: NSDictionary) {
        id = dict.parseString(param: "id")
        name = dict.parseString(param: "name")
        machine_list = dict.parseString(param: "machine_list")
//        machine_list = dict.parseString(param: "machine_list").split(separator: ",").map { String($0) }
    }
}
