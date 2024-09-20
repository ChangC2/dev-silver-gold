import UIKit

class HomeMachineCell: UITableViewCell {
    
    var machine = MMachine()
    var vc: BaseVC?
    
    @IBOutlet weak var vBack: UIView!
    @IBOutlet weak var iMachine: UIImageView!
    @IBOutlet var lMachine: UILabel!
    @IBOutlet var imageWidth: NSLayoutConstraint!
    @IBOutlet var vStatus: UIView!
    @IBOutlet var lStatus: UILabel!
    @IBOutlet var lUtilization: UILabel!
    @IBOutlet var lElapsedTime: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        vBack.setCornerRadius(radius: 12)
        vStatus.setCornerRadius(radius: 8)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func generateCell(machine: MMachine){
        self.machine = machine
        vBack.setBorder(color: UIColor(hex: machine.color) ?? .gray, width: 1)
        var imageUrl = machine.machine_picture_url
        if !imageUrl.contains("http"){
            imageUrl = "https://slymms.com/backend/images/machine/\(imageUrl.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed) ?? "blank%20machine.png")"
        }
        iMachine.sd_setImage(with: URL(string: imageUrl)) { (image, error, cache, url) in
            let width = image == nil ? 100 : self.iMachine.frame.height * image!.size.width / image!.size.height
            self.imageWidth.constant = width < 180 ? width : 180
        }
        lMachine.text = machine.machine_id
        vStatus.backgroundColor = UIColor(hex: machine.color) ?? .gray
        lStatus.textColor = UIColor(hex: machine.color) ?? .gray
        lStatus.text = "\(machine.status)"
        lUtilization.text = "\(round(machine.utilization))%"
        let hours = Int(machine.elapsed / 60 / 60 )
        let mins = Int((machine.elapsed - hours * 60 * 60) / 60)
        let seconds = machine.elapsed - hours * 60 * 60 - mins * 60
        lElapsedTime.text = "(Elapsed : \(String(format: "%02d:%02d:%02d", hours, mins, seconds)))"
//        lElapsedTime.isHidden = machine.status.elementsEqual("Offline")
    }

}
