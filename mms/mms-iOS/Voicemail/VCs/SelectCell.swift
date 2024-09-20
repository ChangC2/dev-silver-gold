//
//  CountryCell.swift
//  Sagor
//
//  Created by SilverGold on 1/12/21.
//  Copyright © 2021 Sagor. All rights reserved.
//

import UIKit

class SelectCell: UITableViewCell {

    @IBOutlet weak var lName: UILabel!
    @IBOutlet weak var bDismiss: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
