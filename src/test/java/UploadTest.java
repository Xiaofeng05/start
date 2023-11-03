import org.junit.jupiter.api.Test;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 23:05
 * @Description:
 */
public class UploadTest {

    @Test
    public void test1(){
        String fileName = "01.jpg";
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(prefix);
    }
}
