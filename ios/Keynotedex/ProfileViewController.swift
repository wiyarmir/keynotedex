import common
import UIKit

let cellReuseIdentifier = "cell"

class ProfileViewController: UIViewController, ProfileView {

    let dataSource = DataSource()
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    
    @IBOutlet weak var tableView: UITableView!
    
    var presenter: ProfilePresenter!
    
    func showProfile(profile: UserProfile) {
        titleLabel.text = profile.user.displayName
        descriptionLabel.text = profile.user.bio ?? "No bio"
        dataSource.items = profile.sessions
        tableView.reloadData()
    }
    
    func showLoading() {
        titleLabel.text = "Loading"
        descriptionLabel.text = ""
   }
    
    func showError(error: String) {
        print(error)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: cellReuseIdentifier)
        tableView.dataSource = dataSource
        presenter = appDelegate.notDagger.profilePresenter(view: self)
    }
    
    deinit {
        presenter.destroy()
    }
    
    @objc
    class DataSource: NSObject, UITableViewDataSource {
        var items = [Session_]()
        
        func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            return items.count
        }
        
        func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
            let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellReuseIdentifier) as UITableViewCell!
            let item = items[indexPath.row]
            cell.textLabel?.text = item.title
            cell.detailTextLabel?.text = item.abstract
            return cell
        }
    }
}
