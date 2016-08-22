package bean;

import javax.ejb.Remote;

import model.Users;

@Remote
public interface UsersBeanRemote {

	public Users getUserByName(String username);

    public void updateUser(Users user);
}
