import Foundation
import UIKit


protocol ClickDelegate {
    func onClick(index: Int, type: Int)
}


protocol SelectDelegate{
    func onSelect(id: String, type: Int)
    func onSelect(index: Int)
}


protocol SearchSelectDelegate {
    func onSelect(type: Int, id: String, name: String)
}

protocol InputBoxDelegate {
    func onInput(text: String)
}
