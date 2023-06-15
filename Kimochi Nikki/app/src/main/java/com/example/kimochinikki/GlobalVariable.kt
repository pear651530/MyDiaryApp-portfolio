import android.app.Application

class GlobalVariable : Application() {
    companion object {
        //存放變數
        private var globel_user_name: String = ""
        private var globel_user_imgURL: String = ""


        //修改 變數値
        fun setName(name: String){
            this.globel_user_name = name
        }
        fun setURL(new_URL: String){
            this.globel_user_name = new_URL
        }

        //取得 變數值
        fun getName(): String{
            return globel_user_name
        }
        fun getURL(): String{
            return globel_user_imgURL
        }
    }
}