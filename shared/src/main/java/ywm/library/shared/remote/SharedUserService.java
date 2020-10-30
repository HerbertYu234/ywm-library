package ywm.library.shared.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ywm.library.shared.model.UserDTO;

/**
 * Created by Herbert Yu on 2020-10-27 22:23
 */
@FeignClient(value = "gateway", path = "user/user")
public interface SharedUserService {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    UserDTO findById(@PathVariable("id") String id);


    @RequestMapping("/get_by_username")
    UserDTO findByUsername(@RequestParam(value = "username") String username);
}
