import java.util.List;
import com.beans.UserInfo;
import com.jdbc.DBUtil;
public class Test_SQL {
    public static void main(String[] args) {
//        demo1();
//        demo2();
//        demo3();
//        demo4();
//        demo5();
//        demo6();
//        demo7();
    }

    static void demo7() {
        String sql="insert into userInfo (userName,password,note) values (?,?,?)";
        int id=DBUtil.addWithId(sql, "川建国","cjg","爱党人士");
        System.out.println("添加成功,生成的主键是:"+ id);
    }
    static void demo6() {
        String sql="select count(*) from userInfo";
        long  count=DBUtil.getScalar(sql);
        int n=Integer.parseInt(count+"");
        System.out.println("总数是:"+n);
    }

    static void demo5() {
        String sql="select userName from userInfo where id=?";
        String userName=DBUtil.getScalar(sql, 4);
        System.out.println(userName);
    }

    static void demo4() {
        String sql="select * from userInfo where id=?";
        UserInfo user=DBUtil.getSingleObj(sql, UserInfo.class, 9);

        user.setUserName("网盘用户");
        user.setPassword("网盘密码");
        user.setPhone("网盘电话");
        user.setNote("网盘备注");

        String sql2="update userInfo set userName=? , password=?, note=?, phone=? where id=? " ;

        Object[] params= {
                user.getUserName(),
                user.getPassword(),
                user.getNote(),
                user.getPhone(),
                user.getId()
        };

        int result=DBUtil.update(sql2,params);
        System.out.println(result==1?"成功":"失败");
    }

    //查询列表
    static void demo3() {
        List<UserInfo> users = DBUtil.getList("select * from userInfo",UserInfo.class);

        for(UserInfo u:users) {
            System.out.println(u);
        }
    }

    //查询
    static void demo2() {
        String sql="select * from userInfo where id=?";
        UserInfo user=DBUtil.getSingleObj(sql, UserInfo.class, 4);
        System.out.println(user);
    }

    //添加
    static void demo1() {
        String sql="insert into userInfo (userName,password,note) values (?,?,?)";
        int result=DBUtil.update(sql, "admin","123","网盘管理员");
        System.out.println(result==1?"成功":"失败");
    }
}