import com.example.mybaseproject.constants.Server

object DatasourceProperties {
    private const val serverType: Int = Server.LIVE

    const val HttpUserAgent = "VNPAY-FLOWER"

    const val TIMEOUT_CONNECT: Long = 90
    const val TIMEOUT_READ: Long = 90
    val LICENSE = "T+0NxJQZMneQa5UTpwO4ubK9bvkky22kD804r2TrSFw6MtjKDaHdhAuaV7P4GM1U6s7oHC/yu3L5nH+Ba57q4OkLT7fBcMJBcDXpi2NT6rKoju76D/ugR1BvCQV+YGaODfULDwYBqNGM3BuGHOzNeSxuJB7kWdwFhHwm4z+x7NYA+VAKE4XQDiMfQUgD+a/UESfKyM+QLTk6EpNZcGMWKjIDSG4CO6m5bZxZzUXSrzbEtc20fE4dJX8BtSVv5K7g50t7d9dtF2Euw5Y02/a9GU1Hg+Smht6/QE2pX0a2tjlvJP/4RNMYDpPmx/o8/syQ3L5mDCV8e8mjsleWabLGCJHaO0/TqAgDxduo9izgnrSpv54unbtC/5bXcVQ5IzqoL2+h8pKEpntUOYvOjtJe4KfkdKe11pi8w8r6lY0R0UpGa717LXyeUwOIOrEAbAnRDLCPsSX/2HmJSh2yXQxvSH3Wtcmx8jwr6KnLrOsypwn1UjdlP8urIyA37CGq9V5zXv3QdQQPyAcccYV8EmhEjx7DCE6WrJzMA274v+SXWKoU29XTQ3bbecndptaVqTnFhGdYcrFIvF+f6uktn14sxBP+t6sqCrdktirL0D32EJRGFL4hzh5kFD4VcyEgPbLvW3rSkI++mhKSGg9c0PEn1nYuBvDnmO0iv8iHrywrrAq8t+c9MFSVo7PvKw=="
     //https://gorest.co.in/public-api/categories/1
    //http://ip.jsontest.com/123
    const val SERVER_URL = "https://gorest.co.in"
    const val DOMAIN = "gorest.co.in"


    fun getLicense(): String {
        return LICENSE
    }
    fun getKey(): String {
        return "66572d3c514a7048"
    }
}