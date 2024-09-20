import Foundation
import UIKit
import Alamofire
// UIView
extension UIView {
    func makeRounded(color: UIColor, borderWidth: CGFloat) {
        self.layer.borderWidth = borderWidth
        self.layer.masksToBounds = false
        self.layer.borderColor = color.cgColor
        self.layer.cornerRadius = self.frame.height / 2
        self.clipsToBounds = true
    }
    
    func image() -> UIImage {
        UIGraphicsBeginImageContextWithOptions(bounds.size, isOpaque, 0)
        
        guard let context = UIGraphicsGetCurrentContext() else {
            return UIImage()
        }
        
        layer.render(in: context)
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image!
    }
    
    func roundCorners(corners: UIRectCorner, radius: CGFloat) {
        let path = UIBezierPath(roundedRect: bounds, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        layer.mask = mask
    }
    
    func applyGradient(colors: [UIColor]) -> Void {
        self.applyGradient(colors: colors, locations: nil)
    }
    
    func applyGradient(colors: [UIColor], locations: [NSNumber]?) -> Void {
        let gradient: CAGradientLayer = CAGradientLayer()
        gradient.frame = self.bounds
        gradient.colors = colors.map { $0.cgColor }
        gradient.locations = locations
        self.layer.insertSublayer(gradient, at: 0)
    }
}

// Data
extension Data {
    var hexString: String {
        let hexString = map { String(format: "%02.2hhx", $0) }.joined()
        return hexString
    }
}

// String
extension String {
    var isArabic: Bool {
        let predicate = NSPredicate(format: "SELF MATCHES %@", "(?s).*\\p{Arabic}.*")
        return predicate.evaluate(with: self)
    }
    
    enum RegularExpressions: String {
        case phone = "^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*(\\d+))?)\\s*$"
    }
    
    func isValid(regex: RegularExpressions) -> Bool {
        return isValid(regex: regex.rawValue)
    }
    
    func isValid(regex: String) -> Bool {
        let matches = range(of: regex, options: .regularExpression)
        return matches != nil
    }
    
    func onlyDigits() -> String {
        let filtredUnicodeScalars = unicodeScalars.filter{CharacterSet.decimalDigits.contains($0)}
        return String(String.UnicodeScalarView(filtredUnicodeScalars))
    }
    
    func makeCall() {
        if isValid(regex: .phone) {
            if let url = URL(string: "tel://\(self)"), UIApplication.shared.canOpenURL(url) {
                if #available(iOS 10, *) {
                    UIApplication.shared.open(url)
                } else {
                    UIApplication.shared.openURL(url)
                }
            }
        }
    }
    
    var urlEncoded: String? {
        return addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
    }
    
    static func format(decimal:Float, _ maximumDigits:Int = 1, _ minimumDigits:Int = 1) ->String? {
        let number = NSNumber(value: decimal)
        let numberFormatter = NumberFormatter()
        numberFormatter.maximumFractionDigits = maximumDigits
        numberFormatter.minimumFractionDigits = minimumDigits
        return numberFormatter.string(from: number)
    }
}

extension NSAttributedString {
    convenience init(htmlString html: String, font: UIFont? = nil, useDocumentFontSize: Bool = true) throws {
        let options: [NSAttributedString.DocumentReadingOptionKey : Any] = [
            .documentType: NSAttributedString.DocumentType.html,
            .characterEncoding: String.Encoding.utf8.rawValue
        ]
        
        let data = html.data(using: .utf8, allowLossyConversion: true)
        guard (data != nil), let fontFamily = font?.familyName, let attr = try? NSMutableAttributedString(data: data!, options: options, documentAttributes: nil) else {
            try self.init(data: data ?? Data(html.utf8), options: options, documentAttributes: nil)
            return
        }
        
        let fontSize: CGFloat? = useDocumentFontSize ? nil : font!.pointSize
        let range = NSRange(location: 0, length: attr.length)
        attr.enumerateAttribute(.font, in: range, options: .longestEffectiveRangeNotRequired) { attrib, range, _ in
            if let htmlFont = attrib as? UIFont {
                let traits = htmlFont.fontDescriptor.symbolicTraits
                var descrip = htmlFont.fontDescriptor.withFamily(fontFamily)
                
                if (traits.rawValue & UIFontDescriptor.SymbolicTraits.traitBold.rawValue) != 0 {
                    if let boldDescrip = descrip.withSymbolicTraits(.traitBold) {
                        descrip = boldDescrip
                    }
                }
                
                if (traits.rawValue & UIFontDescriptor.SymbolicTraits.traitItalic.rawValue) != 0 {
                    if let italicDescrip = descrip.withSymbolicTraits(.traitItalic) {
                        descrip = italicDescrip
                    }
                }
                
                attr.addAttribute(.font, value: UIFont(descriptor: descrip, size: fontSize ?? htmlFont.pointSize), range: range)
            }
        }
        
        self.init(attributedString: attr)
    }
}

// UISearchBar
extension UISearchBar {
    func change(textFont : UIFont?) {
        for view : UIView in (self.subviews[0]).subviews {
            if let textField = view as? UITextField {
                textField.font = textFont
            }
        }
    }
}



extension UIColor{
    convenience init?(hex: String) {
        var hexSanitized = hex.trimmingCharacters(in: .whitespacesAndNewlines)
        hexSanitized = hexSanitized.replacingOccurrences(of: "#", with: "")
        
        var rgb: UInt64 = 0
        
        guard Scanner(string: hexSanitized).scanHexInt64(&rgb) else { return nil }
        
        let length = hexSanitized.count
        let r, g, b, a: CGFloat
        if length == 6 {
            r = CGFloat((rgb & 0xFF0000) >> 16) / 255.0
            g = CGFloat((rgb & 0x00FF00) >> 8) / 255.0
            b = CGFloat(rgb & 0x0000FF) / 255.0
            a = 1.0
        } else if length == 8 {
            r = CGFloat((rgb & 0xFF000000) >> 24) / 255.0
            g = CGFloat((rgb & 0x00FF0000) >> 16) / 255.0
            b = CGFloat((rgb & 0x0000FF00) >> 8) / 255.0
            a = CGFloat(rgb & 0x000000FF) / 255.0
        } else {
            return nil
        }
        
        self.init(red: r, green: g, blue: b, alpha: a)
    }
    
    enum EqualNoteColor{
        //        case: appDefaultColor
        case appDefaultColor
        //case gray
        
        func color(with alpha: CGFloat) -> UIColor {
            var colorToReturn:UIColor?
            switch self {
            case .appDefaultColor:
                colorToReturn = UIColor(red: 255/255, green: 22/255, blue: 84/255, alpha: alpha)
                return colorToReturn!
            }
            
        }
    }
}

extension UIView {
    
    enum ViewSide {
        case Left, Right, Top, Bottom
    }

    func addBorder(toSide side: ViewSide, withColor color: CGColor, andThickness thickness: CGFloat) {

        let border = CALayer()
        border.backgroundColor = color

        switch side {
        case .Left: border.frame = CGRect(x: frame.minX, y: frame.minY, width: thickness, height: frame.height); break
        case .Right: border.frame = CGRect(x: frame.maxX, y: frame.minY, width: thickness, height: frame.height); break
        case .Top: border.frame = CGRect(x: frame.minX, y: frame.minY, width: frame.width, height: thickness); break
        case .Bottom: border.frame = CGRect(x: frame.minX, y: frame.maxY, width: frame.width, height: thickness); break
        }

        layer.addSublayer(border)
    }
    
    func setCornerRadius(radius: CGFloat) {
        self.layer.cornerRadius = radius
        self.layer.masksToBounds = true
    }
    
    func setLeftCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMinXMinYCorner, .layerMinXMaxYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setTopCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setBottomCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMinXMaxYCorner, .layerMaxXMaxYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setRightCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMaxXMaxYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topRight, .bottomRight],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setTopLeftCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMinXMinYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setTopRightCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMaxXMinYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setBottomRightCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMaxXMaxYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func setBottomLeftCornerRadius(radius: CGFloat) {
        if #available(iOS 11.0, *){
          self.clipsToBounds = true
          self.layer.cornerRadius = radius
          self.layer.maskedCorners = [.layerMinXMaxYCorner]
        }else{
          let path = UIBezierPath(roundedRect: self.bounds,byRoundingCorners:[.topLeft, .bottomLeft],cornerRadii: CGSize(width: radius, height:  radius))
          let maskLayer = CAShapeLayer()
          maskLayer.path = path.cgPath
          self.layer.mask = maskLayer
        }
    }
    
    func showView() {
        self.isHidden = false
    }
    
    func hideView() {
        self.isHidden = true
    }
    
    func setBorderShadow(){
        layer.borderWidth = 1
        layer.borderColor = #colorLiteral(red: 0.8039215803, green: 0.8039215803, blue: 0.8039215803, alpha: 1)
        layer.shadowColor = #colorLiteral(red: 0.8039215803, green: 0.8039215803, blue: 0.8039215803, alpha: 1).cgColor
        layer.shadowOffset = CGSize(width: 0.0, height: 0.0)
        layer.shadowRadius = 4
        layer.shadowOpacity = 0.8
        layer.cornerRadius = 6
    }
    
    func setBorder1(){
        layer.shadowColor = #colorLiteral(red: 0.8039215803, green: 0.8039215803, blue: 0.8039215803, alpha: 1).cgColor
        layer.shadowOffset = CGSize(width: 0.0, height: 0.0)
        layer.shadowRadius = 4
        layer.shadowOpacity = 0.8
        layer.cornerRadius = 10
        layer.borderWidth = 1
        layer.borderColor = #colorLiteral(red: 0.8039215803, green: 0.8039215803, blue: 0.8039215803, alpha: 1)
    }
    
    func setBorder2(){
        layer.borderWidth = 1
        layer.borderColor = #colorLiteral(red: 1, green: 0.2527923882, blue: 1, alpha: 1)
        layer.shadowColor = #colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1).cgColor
        layer.shadowOffset = .zero
        layer.shadowOpacity = 0.7
        layer.shadowRadius = 5
        layer.cornerRadius = 12
    }
    
    func setBorder3(){
        layer.borderWidth = 1
        layer.borderColor = UIColor(named: "appMainColor")!.cgColor
        layer.shadowColor = #colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1).cgColor
        layer.shadowOffset = .zero
        layer.shadowOpacity = 0.7
        layer.shadowRadius = 12
        layer.cornerRadius = 12
    }
    
    func setBorder4(){
        layer.borderWidth = 2
        layer.borderColor = #colorLiteral(red: 1, green: 0.5781051517, blue: 0, alpha: 0.25)
        layer.shadowColor = #colorLiteral(red: 0.8039215803, green: 0.8039215803, blue: 0.8039215803, alpha: 1).cgColor
        layer.shadowOffset = .zero
        layer.shadowOpacity = 0.3
        layer.shadowRadius = 1
        layer.cornerRadius = 12
    }
    
    func setBorder5(){
        layer.borderWidth = 1
        layer.borderColor = #colorLiteral(red: 0.501960814, green: 0.501960814, blue: 0.501960814, alpha: 1)
        layer.cornerRadius = 7
    }
    
    func setBorder6(){
        layer.borderWidth = 1
        layer.borderColor = #colorLiteral(red: 0.2392156869, green: 0.6745098233, blue: 0.9686274529, alpha: 1)
        layer.cornerRadius = 12
    }
    
    
    func setShadow(radius: CGFloat, opacity: Float) {
        let shadow = UIView(frame: self.frame)
        shadow.backgroundColor = .white
        shadow.isUserInteractionEnabled = false
        shadow.layer.shadowColor = UIColor.black.cgColor
        shadow.layer.shadowOffset = .zero
        shadow.layer.shadowRadius = radius
        shadow.layer.masksToBounds = false
        shadow.layer.cornerRadius = self.layer.cornerRadius
        shadow.layer.shadowOpacity = opacity
        self.superview?.addSubview(shadow)
        self.superview?.sendSubviewToBack(shadow)
    }
    
    func setBorder(color: UIColor, width: CGFloat) {
        self.layer.borderColor = color.cgColor
        self.layer.borderWidth = width
    }
    
    func generateShadowUsingBezierPath(radius: CGFloat, opacity: Float)  {
        self.layer.cornerRadius = 4.0
        self.layer.borderWidth = 1.0
        self.layer.borderColor = UIColor.clear.cgColor
        self.layer.masksToBounds = true
        
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        self.layer.shadowRadius = radius
        self.layer.shadowOpacity = opacity
        self.layer.masksToBounds = false
        self.layer.shadowPath = UIBezierPath(roundedRect: self.bounds, cornerRadius: self.layer.cornerRadius).cgPath
    }
    
    func fadeIn(duration: TimeInterval = 1.0) {
        UIView.animate(withDuration: duration, animations: {
            self.alpha = 1.0
        })
    }
    
    func fadeOut(duration: TimeInterval = 1.0) {
        UIView.animate(withDuration: duration, animations: {
            self.alpha = 0.0
        })
    }
    
    var heightConstaint: NSLayoutConstraint? {
        get {
            return constraints.first(where: {
                $0.firstAttribute == .height && $0.relation == .equal
            })
        }
        set { setNeedsLayout() }
    }
    
    var widthConstaint: NSLayoutConstraint? {
        get {
            return constraints.first(where: {
                $0.firstAttribute == .width && $0.relation == .equal
            })
        }
        set { setNeedsLayout() }
    }

}

// MARK: Textfield
extension UITextField {
    
    var isEmpty: Bool {
        if self.text == nil || self.text == "" || self.text!.trimmingCharacters(in: .whitespaces) == "" {
            return true
        }
        return false
    }
    
    var isValidEmail: Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: self.text!)
    }
    
    var isValidPassword: Bool{
        if self.text!.count >= 6 {
            return true
        }
        return false
    }
    
    var isValidPhoneNo: Bool{
        if self.text!.count >= 6 && self.text!.count <= 14 {
            return true
        }
        return false
    }
    
    var isValidName: Bool{
        if self.text!.count >= 3 && self.text!.count <= 120 {
            return true
        }
        return false
    }
    
    var isValidText: Bool{
        if self.text!.count >= 2 && self.text!.count <= 200 {
            return true
        }
        return false
    }
    
    func whitePlaceholder(placeholder: String){
        self.attributedPlaceholder = NSAttributedString(string: placeholder,
                             attributes: [NSAttributedString.Key.foregroundColor: UIColor.lightGray])
    }
}

extension UIFont {
    
    enum RqualNoteFont: String {
        case black = "Raleway-Black"
        case bold = "Raleway-Bold"
        case medium = "Raleway-Medium"
        case regular = "Raleway-Regular"
        
        func fontWithSize(size: CGFloat) -> UIFont {
            return UIFont(name: rawValue, size: size)!
        }
    }
}

extension  UITextView{
    
    var isEmpty: Bool {
        if self.text == nil || self.text == "" || self.text!.trimmingCharacters(in: .whitespaces) == "" {
            return true
        }
        return false
    }
}

enum DateFormat: String {
    case dateAndTimea = "MM-dd-yyyy hh:mm a"
    case dateAndTimeAndSecs = "yyyy-MM-dd hh:mm:ss a"
    case dateTimeee = "yyyy-MM-dd HH:mm:ss"
    case dateSlash = "yyyy-MM-dd"
    case dateTime = "yyyy/MM/dd hh:mm a"
    case date = "MM/yyyy"
    case dateFormatToWebEnd = "MM-YYYY"
    case dateTimeUTC = "yyyy-MM-dd'T'HH:mm:ss"
    case dateTimeUTC1 = "yyyy-MM-ddTHH:mm:ss"
    case dateNow = "yyyy/MM/dd"
    case time = "hh:mm a"
    case times = "hh:mm:ss a"
    case dateWeek = "E HH:mm"
    case weekMonthDay = "EEEE, MMM d"
    case weekDayMonthYear = "E dd MMM yyyy"
    case weekMonthDayTime = "EEEE MM-dd h:mm a"
    case hhmm = "HH:mm"
    case MMMdd = "MMM dd"
    case MMddHHmm = "MMM dd HH:mm"
    case MMddyyyy = "MM/dd/yyyy"
    case ddMMyyyy = "dd/MM/yyyy"
    case ddMMMyyyy = "dd MMM, yyyy"
    case ddMMMyyyyhhmma = "dd MMM, yyyy | hh:mm a"
    case yyyyMMddhhmma = "yyyy-MM-dd hh:mm a"
    case yyyyMMddhhmm = "yyyy-MM-dd HH:mm"
    case MMddyyyyHHmm = "MM/dd/yyyy HH:mm"
}



extension TimeInterval {
    func convertSecondString() -> String {
        let component =  Date.dateComponentFrom(second: self)
        if let hour = component.hour ,
            let min = component.minute ,
            let sec = component.second {
            
            let fix =  hour > 0 ? NSString(format: "%02d:", hour) : ""
            let a = NSString(format: "%@%02d:%02d", fix,min,sec) as String
            return a
        } else {
            return "-:-"
        }
    }
}

extension Date {
    static func dateComponentFrom(second: Double) -> DateComponents {
        let interval = TimeInterval(second)
        let date1 = Date()
        let date2 = Date(timeInterval: interval, since: date1)
        let c = NSCalendar.current
        
        var components = c.dateComponents([.year,.month,.day,.hour,.minute,.second,.weekday], from: date1, to: date2)
        components.calendar = c
        return components
    }
    
    static func timeAgoSinceDate(_ date:Date, numericDates:Bool = false) -> String {
        let calendar = NSCalendar.current
        let unitFlags: Set<Calendar.Component> = [.minute, .hour, .day, .weekOfYear, .month, .year, .second]
        let now = Date()
        let earliest = now < date ? now : date
        let latest = (earliest == now) ? date : now
        let components = calendar.dateComponents(unitFlags, from: earliest,  to: latest)
        
        if (components.year! >= 2) {
            return "\(components.year!) years ago"
        } else if (components.year! >= 1){
            if (numericDates){
                return "1 year ago"
            } else {
                return "Last year"
            }
        } else if (components.month! >= 2) {
            return "\(components.month!) months ago"
        } else if (components.month! >= 1){
            if (numericDates){
                return "1 month ago"
            } else {
                return "Last month"
            }
        } else if (components.weekOfYear! >= 2) {
            return "\(components.weekOfYear!) weeks ago"
        } else if (components.weekOfYear! >= 1){
            if (numericDates){
                return "1 week ago"
            } else {
                return "Last week"
            }
        } else if (components.day! >= 2) {
            return "\(components.day!) days ago"
        } else if (components.day! >= 1){
            if (numericDates){
                return "1 day ago"
            } else {
                return "Yesterday"
            }
        } else if (components.hour! >= 2) {
            return "\(components.hour!) hours ago"
        } else if (components.hour! >= 1){
            if (numericDates){
                return "1 hour ago"
            } else {
                return "An hour ago"
            }
        } else if (components.minute! >= 2) {
            return "\(components.minute!) minutes ago"
        } else if (components.minute! >= 1){
            if (numericDates){
                return "1 minute ago"
            } else {
                return "A minute ago"
            }
        } else if (components.second! >= 3) {
            return "\(components.second!) seconds ago"
        } else {
            return "Just now"
        }
    }
    
    
       
       func isToday() -> Bool {
           return Calendar.current.isDateInToday(self)
       }
       
       var hour: Int{
           let calendar = Calendar.current
           return calendar.component(.hour, from: self)
       }
       var minute: Int{
           let calendar = Calendar.current
           return calendar.component(.minute, from: self)
       }
       
       var second: Int{
           let calendar = Calendar.current
           return calendar.component(.second, from: self)
       }
       
       var year: Int{
           let calendar = Calendar.current
           return calendar.component(.year, from: self)
       }
       
       var month: Int{
           let calendar = Calendar.current
           return calendar.component(.month, from: self)
       }
       
       var day: Int{
           let calendar = Calendar.current
           return calendar.component(.day, from: self)
       }
       
       var weekday: Int{
           let calendar = Calendar.current
           return calendar.component(.weekday, from: self)
       }
       
       
       func timeRemaining(numericDates:Bool = false) -> String {
           let calendar = NSCalendar.current
           let unitFlags: Set<Calendar.Component> = [.minute, .hour, .day, .weekOfYear, .month, .year, .second]
           let now = Date()
           
           if now >= self {
               return "Expired"
           }
           
           let components = calendar.dateComponents(unitFlags, from: now,  to: self)
           
           if (components.year! >= 2) {
               return "\(components.year!) years remaining"
           } else if (components.year! >= 1){
               return "1 year remaining"
           } else if (components.month! >= 2) {
               return "\(components.month!) months remaining"
           } else if (components.month! >= 1){
               return "1 month"
           } else if (components.weekOfYear! >= 2) {
               return "\(components.weekOfYear!) weeks remaining"
           } else if (components.weekOfYear! >= 1){
               return "1 week remaining"
           } else if (components.day! >= 2) {
               return "\(components.day!) days remaining"
           } else if (components.day! >= 1){
               return "1 day remaining"
           } else if (components.hour! >= 2) {
               return "\(components.hour!) hours remaining"
           } else if (components.hour! >= 1){
               if (numericDates) {
                   return "1 hour remaining"
               } else {
                   return "An hour remaining"
               }
           } else if (components.minute! >= 2) {
               return "\(components.minute!) minutes remaining"
           } else if (components.minute! >= 1){
               if (numericDates){
                   return "1 minute remaining"
               } else {
                   return "A minute remaining"
               }
           } else if (components.second! >= 3) {
               return "\(components.second!) seconds remaining"
           } else {
               return "Just now"
           }
       }
    
        func stringFromDate(format: DateFormat) -> String {
           let dateFormatter = DateFormatter()
           dateFormatter.dateFormat = format.rawValue
           dateFormatter.timeZone = NSTimeZone(name: "UTC") as TimeZone?
           return dateFormatter.string(from: self)
        }
       
        func localTimeString(format: DateFormat)->String{
           let dateFormatter = DateFormatter()
           dateFormatter.dateFormat = format.rawValue
           dateFormatter.timeZone = .current
           return dateFormatter.string(from: self)
        }
       
       var startOfWeek: Date? {
           let gregorian = Calendar(identifier: .gregorian)
           guard let sunday = gregorian.date(from: gregorian.dateComponents([.yearForWeekOfYear, .weekOfYear], from: self)) else { return nil }
           return gregorian.date(byAdding: .day, value: 1, to: sunday)
       }

       var endOfWeek: Date? {
           let gregorian = Calendar(identifier: .gregorian)
           guard let sunday = gregorian.date(from: gregorian.dateComponents([.yearForWeekOfYear, .weekOfYear], from: self)) else { return nil }
           return gregorian.date(byAdding: .day, value: 7, to: sunday)
       }
    
    
    func daysAgo(days: Int) -> Date?{
        return Calendar.current.date(byAdding: .day, value: days, to: self)
    }
    
    var timestamp:Int64 {
        return Int64((self.timeIntervalSince1970 * 1000.0).rounded())
    }

    init(timestamp:Int64) {
        self = Date(timeIntervalSince1970: TimeInterval(timestamp) / 1000)
    }
}

extension String {
    func unespecial()->String{
        let data = self.data(using: String.Encoding.utf8);
        let decodedStr = NSString(data: data!, encoding:String.Encoding.nonLossyASCII.rawValue)
        if let str = decodedStr{
            return unespecial(str: str as String)
        }
        return unespecial(str: self)
    }
    
    var clearurl : String   {
        let url =  self.removingPercentEncoding ?? ""
        return  url.replacingOccurrences(of: "\"", with: "", options: .literal, range: nil)
    }
    
    func unespecial(str: String)->String{
        let mutableString = NSMutableString(string: str)
        CFStringTransform(mutableString, nil, "Any-Hex/Java" as NSString, true)
        return mutableString as String
    }
    
    var htmlToAttributedString: NSAttributedString? {
        guard let data = data(using: .utf8) else { return nil }
        do {
            return try NSAttributedString(data: data, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding:String.Encoding.utf8.rawValue], documentAttributes: nil)
        } catch {
            return nil
        }
    }
    
    var htmlToString: String {
        return htmlToAttributedString?.string ?? ""
    }
    
    func containsIgnoringCase(find: String) -> Bool{
        return self.range(of: find, options: .caseInsensitive) != nil
    }
    
    func dateFromString(format: DateFormat) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format.rawValue
        dateFormatter.timeZone = NSTimeZone(abbreviation: "UTC") as TimeZone?
        dateFormatter.locale =  Locale.current
        return dateFormatter.date(from: self)
    }
    
    func toDateFormattedWith(format:String)-> NSDate? {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.date(from: self) as NSDate?
    }
    
    func getDate()-> NSDate?{
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = NSTimeZone(abbreviation: "UTC") as TimeZone?
        let dataDate = dateFormatter.date(from: self) as NSDate?
        return dataDate
    }
    
    // Check Email Validation
    func isValidEmail() -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: self)
    }
    
    func isValidPhoneNumber() -> Bool {
        let range = NSRange(location: 0, length: self.count)
        let regex = try! NSRegularExpression(pattern: "(\\([0-9]{3}\\) |[0-9]{3}-)[0-9]{3}-[0-9]{4}")
        if regex.firstMatch(in: self, options: [], range: range) != nil{
            return true
        }else{
            return false
        }
    }
    
    // Check Password complexity
    func isValidPassword()->Bool{
        let capitalLetterRegEx  = ".*[A-Z]+.*"
        let texttest = NSPredicate(format:"SELF MATCHES %@", capitalLetterRegEx)
        let capitalresult = texttest.evaluate(with: self)
        
        let numberRegEx  = ".*[0-9]+.*"
        let texttest1 = NSPredicate(format:"SELF MATCHES %@", numberRegEx)
        let numberresult = texttest1.evaluate(with: self)
        
//        let specialCharacterRegEx  = ".*[!&^%$#@()/]+.*"
//        let texttest2 = NSPredicate(format:"SELF MATCHES %@", specialCharacterRegEx)
//        let specialresult = texttest2.evaluate(with: self)

        return capitalresult && numberresult// && specialresult
    }
    
    func isPass8Length()->Bool{
        if self.count < 8{
            return false
        }else{
            return true
        }
    }
    
    func isPassUpper()->Bool{
        let capitalLetterRegEx  = ".*[A-Z]+.*"
        let texttest = NSPredicate(format:"SELF MATCHES %@", capitalLetterRegEx)
        return texttest.evaluate(with: self)
    }
    
    func isPassNum()->Bool{
        let numberRegEx  = ".*[0-9]+.*"
        let texttest1 = NSPredicate(format:"SELF MATCHES %@", numberRegEx)
        return texttest1.evaluate(with: self)
    }
    
    var isNumber: Bool {
        return !isEmpty && rangeOfCharacter(from: CharacterSet.decimalDigits.inverted) == nil
    }
    
    func capitalizingFirstLetter() -> String {
        return prefix(1).capitalized + dropFirst()
    }
    
    mutating func capitalizeFirstLetter() {
        self = self.capitalizingFirstLetter()
    }
    
    var floatValue: Float {
        return (self as NSString).floatValue
    }
    
    var intValue: Int{
        return (self as NSString).integerValue
    }
    
    var doubleValue: Double{
        return (self as NSString).doubleValue
    }
    
    func withCommas() -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        return formatter.string(from: NSNumber(value:self.floatValue))!
        
//        let lVal = self.floatValue - Float(self.intValue)
//        if lVal == 0.0{
//           return formatter.string(from: NSNumber(value:self.intValue))!
//        }else{
//            return "\(formatter.string(from: NSNumber(value:self.intValue))!).\(String(format: "%.2f", lVal))"
//        }
    }
    
    func fromJson(json: AnyObject) -> String {
        do {
            let data =  try JSONSerialization.data(withJSONObject: json)
            return String(data: data, encoding: .utf8)!
        } catch {
            return ""
        }
    }
    
    func toJson(string: String) -> Any {
        do {
            let jsonData = string.data(using: .utf8)
            let jsonObject = try JSONSerialization.jsonObject(with: jsonData!, options: .mutableContainers)
            return jsonObject
        } catch {}
        return []
    }
    
    func fromDictionary(dic: NSDictionary) -> String{
        let jsonData = try! JSONSerialization.data(withJSONObject: dic, options: [])
        return String(data: jsonData, encoding: .utf8)!
    }
    
    func toDictionary()->NSDictionary?{
        let data = Data(self.utf8)
        do {
            return try JSONSerialization.jsonObject(with: data, options: []) as? NSDictionary
        } catch {}
        return nil
    }
    
    func toEncoding() -> String? {
        let unreserved = "-._~/?"
        let allowed = NSMutableCharacterSet.alphanumeric()
        allowed.addCharacters(in: unreserved)
        return addingPercentEncoding(withAllowedCharacters: allowed as CharacterSet)
    }
    
    func subString(start: Int, end: Int) -> String{
        var ret = ""
        let start_index = self.index(self.startIndex, offsetBy: start)
        let end_index = self.index(self.startIndex, offsetBy: end)
        let range = start_index..<end_index
        ret = String(self[range])  // play
        return ret
    }
    
    func localized() -> String {
        NSLocalizedString(self, comment: "")
    }
}

extension UIImageView{
    
    func makeCircular(){
        self.clipsToBounds = true
        self.layer.cornerRadius = self.frame.size.width / 2
    }
    
    func makeCircular(borderColor: UIColor){
        self.clipsToBounds = true
        self.layer.cornerRadius = self.frame.size.width / 2
        self.layer.borderColor = borderColor.cgColor
        self.layer.borderWidth = 2
    }
}

extension NSDictionary{
    func parseInt(param: String)->Int{
        let str: String? = "\(self[param] ?? 0)"
        if let val = str{
            return val.intValue
        }else{
            return 0
        }
    }
    
    func parseString(param: String)->String{
        var str: String? = "\(self[param] ?? "")"
        if str!.elementsEqual("null") || str!.elementsEqual("<null>"){
            str = ""
        }
        return str!
    }
    
    func parseDouble(param: String)->Double{
        let str: String? = "\(self[param] ?? 0)"
        if let val = str{
            return val.doubleValue
        }else{
            return 0.0
        }
    }
    
    func parseFloat(param: String)->Float{
        let str: String? = "\(self[param] ?? 0)"
        if let val = str{
            return val.floatValue
        }else{
            return 0.0
        }
    }
    
    func parseBool(param: String)->Bool{
        if let val = self[param] as? String{
            if val == "1"{
                return true
            }else if val == "0"{
                return false
            }
        }
        
        if let val = self[param] as? Bool{
            return val
        }else{
            return false
        }
    }
    
    func toString() -> String{
        let jsonData = try! JSONSerialization.data(withJSONObject: self, options: [])
        return String(data: jsonData, encoding: .utf8)!
    }
}

extension Encodable {
  func asDictionary() throws -> [String: Any] {
    let data = try JSONEncoder().encode(self)
    guard let dictionary = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String: Any] else {
      throw NSError()
    }
    return dictionary
  }
}

extension String: ParameterEncoding {
    public func encode(_ urlRequest: URLRequestConvertible, with parameters: Parameters?) throws -> URLRequest {
        var request = try urlRequest.asURLRequest()
        request.httpBody = data(using: .utf8, allowLossyConversion: false)
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        return request
    }
}

extension UITapGestureRecognizer {

    func didTapAttributedTextInLabel(label: UILabel, inRange targetRange: NSRange) -> Bool {
        // Create instances of NSLayoutManager, NSTextContainer and NSTextStorage
        let layoutManager = NSLayoutManager()
        let textContainer = NSTextContainer(size: CGSize.zero)
        let textStorage = NSTextStorage(attributedString: label.attributedText!)

        // Configure layoutManager and textStorage
        layoutManager.addTextContainer(textContainer)
        textStorage.addLayoutManager(layoutManager)

        // Configure textContainer
        textContainer.lineFragmentPadding = 0.0
        textContainer.lineBreakMode = label.lineBreakMode
        textContainer.maximumNumberOfLines = label.numberOfLines
        let labelSize = label.bounds.size
        textContainer.size = labelSize

        // Find the tapped character location and compare it to the specified range
        let locationOfTouchInLabel = self.location(in: label)
        let textBoundingBox = layoutManager.usedRect(for: textContainer)
        //let textContainerOffset = CGPointMake((labelSize.width - textBoundingBox.size.width) * 0.5 - textBoundingBox.origin.x,
                                              //(labelSize.height - textBoundingBox.size.height) * 0.5 - textBoundingBox.origin.y);
        let textContainerOffset = CGPoint(x: (labelSize.width - textBoundingBox.size.width) * 0.5 - textBoundingBox.origin.x, y: (labelSize.height - textBoundingBox.size.height) * 0.5 - textBoundingBox.origin.y)

        //let locationOfTouchInTextContainer = CGPointMake(locationOfTouchInLabel.x - textContainerOffset.x,
                                                        // locationOfTouchInLabel.y - textContainerOffset.y);
        let locationOfTouchInTextContainer = CGPoint(x: locationOfTouchInLabel.x - textContainerOffset.x, y: locationOfTouchInLabel.y - textContainerOffset.y)
        let indexOfCharacter = layoutManager.characterIndex(for: locationOfTouchInTextContainer, in: textContainer, fractionOfDistanceBetweenInsertionPoints: nil)
        return NSLocationInRange(indexOfCharacter, targetRange)
    }

}

extension UIImage {
    func tinted(color: UIColor) -> UIImage? {
        let image = withRenderingMode(.alwaysTemplate)
        let imageView = UIImageView(image: image)
        imageView.tintColor = color

        UIGraphicsBeginImageContextWithOptions(image.size, false, 0.0)
        if let context = UIGraphicsGetCurrentContext() {
            imageView.layer.render(in: context)
            let tintedImage = UIGraphicsGetImageFromCurrentImageContext()
            UIGraphicsEndImageContext()
            return tintedImage
        } else {
            return self
        }
    }
    
    func resized(toWidth width: CGFloat) -> UIImage? {
        let canvasSize = CGSize(width: width, height: CGFloat(ceil(width/size.width * size.height)))
        UIGraphicsBeginImageContextWithOptions(canvasSize, false, scale)
        defer { UIGraphicsEndImageContext() }
        draw(in: CGRect(origin: .zero, size: canvasSize))
        return UIGraphicsGetImageFromCurrentImageContext()
    }
}

extension UITableView {

    public func reloadData(_ completion: @escaping ()->()) {
        UIView.animate(withDuration: 0, animations: {
            self.reloadData()
        }, completion:{ _ in
            completion()
        })
    }

    func scroll(to: scrollsTo, animated: Bool) {
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(300)) {
            let numberOfSections = self.numberOfSections
            let numberOfRows = self.numberOfRows(inSection: numberOfSections-1)
            switch to{
            case .top:
                if numberOfRows > 0 {
                     let indexPath = IndexPath(row: 0, section: 0)
                     self.scrollToRow(at: indexPath, at: .top, animated: animated)
                }
                break
            case .bottom:
                if numberOfRows > 0 {
                    let indexPath = IndexPath(row: numberOfRows-1, section: (numberOfSections-1))
                    self.scrollToRow(at: indexPath, at: .bottom, animated: animated)
                }
                break
            }
        }
    }

    enum scrollsTo {
        case top,bottom
    }
}

extension Int {
    /**
     * Shorten the number to *thousand* or *million*
     * - Returns: the shorten number and the suffix as *String*
     */
    func shorten() -> String{
        let number = Double(self)
        let thousand = number / 1000
        let million = number / 1000000
        if million >= 1.0 {
            return "\(round(million*10)/10)M"
        }
        else if thousand >= 1.0 {
            return "\(round(thousand*10)/10)K"
        }
        else {
            return "\(self)"
        }
    }
}

extension URL {
    /// Adds the scheme prefix to a copy of the receiver.
    func convertToRedirectURL(scheme: String) -> URL? {
        var components = URLComponents.init(url: self, resolvingAgainstBaseURL: false)
        let schemeCopy = components?.scheme ?? ""
        components?.scheme = schemeCopy + scheme
        return components?.url
    }
    
    /// Removes the scheme prefix from a copy of the receiver.
    func convertFromRedirectURL(prefix: String) -> URL? {
        guard var comps = URLComponents(url: self, resolvingAgainstBaseURL: false) else {return nil}
        guard let scheme = comps.scheme else {return nil}
        comps.scheme = scheme.replacingOccurrences(of: prefix, with: "")
        return comps.url
    }
}


class CustomTextField: UITextField {

    let padding = UIEdgeInsets(top: 0, left: 15, bottom: 0, right: 15);

    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: padding)
    }

    override func placeholderRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: padding)
    }

    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: padding)
    }
}

extension Double {
    
    private var formatter: DateComponentsFormatter {
        let formatter = DateComponentsFormatter()
        formatter.allowedUnits = [.minute, .second]
        formatter.unitsStyle = .positional
        formatter.zeroFormattingBehavior = .pad
        return formatter
    }
    
    func secondsToString() -> String {
        return formatter.string(from: self) ?? ""
    }
    
}

extension UIScrollView {
    func scrollToTop(animated: Bool) {
        let desiredOffset = CGPoint(x: 0, y: -self.contentInset.top)
        self.setContentOffset(desiredOffset, animated: animated)
    }
}
