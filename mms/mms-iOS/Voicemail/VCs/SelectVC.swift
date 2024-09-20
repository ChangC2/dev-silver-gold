import UIKit

class SelectVC: BaseVC {
    
    @IBOutlet weak var vCenter: UIView!
    @IBOutlet weak var tableH: NSLayoutConstraint!
    @IBOutlet weak var tableView: UITableView!
    
    var datas = [String]()
    var delegate: SelectDelegate?
    

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.7)
        vCenter.setBorder5()
        if datas.count < 8{
            tableH.constant = CGFloat(40 * datas.count)
        }else{
            tableH.constant = 280
        }
    }
    @IBAction func bCancalTapped(_ sender: Any) {
        dismiss(animated: true)
    }
    
    @objc func onDismiss(_ sender: UIButton){
        onSelect(index: sender.tag)
    }
    
    func onSelect(index: Int){
        delegate?.onSelect(index: index)
    }
}


extension SelectVC: UITableViewDelegate,UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SelectCell", for: indexPath) as! SelectCell
        cell.selectionStyle = .none
        cell.lName.text = datas[indexPath.row]
        if datas[indexPath.row] == "--- Pro Voices ---" || datas[indexPath.row] == "--- Your Cloned Voices ---"{
            cell.lName.alpha = 0.7
        }
        cell.bDismiss.tag = indexPath.row
        cell.bDismiss.addTarget(self, action: #selector(self.onDismiss(_:)), for: .touchUpInside)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if datas[indexPath.row] == "--- Pro Voices ---" || datas[indexPath.row] == "--- Your Cloned Voices ---"{
            return
        }
        onSelect(index: indexPath.row)
    }
}
