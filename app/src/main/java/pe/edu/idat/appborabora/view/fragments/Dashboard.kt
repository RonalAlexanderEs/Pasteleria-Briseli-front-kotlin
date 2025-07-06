package pe.edu.idat.appborabora.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import pe.edu.idat.appborabora.R
import pe.edu.idat.appborabora.adapter.ProductoDashboardAdapter
import pe.edu.idat.appborabora.view.activities.Login
import pe.edu.idat.appborabora.view.activities.RegisterUser
import pe.edu.idat.appborabora.viewmodel.ProductoDashViewModel

class Dashboard : Fragment(), View.OnClickListener {

    private lateinit var loginOption: RelativeLayout
    private lateinit var rvTopProductos: RecyclerView
    private lateinit var productoDashViewModel: ProductoDashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // UI
        loginOption = view.findViewById(R.id.loginOption)
        rvTopProductos = view.findViewById(R.id.rvTopProductos)
        val btnlogindash: Button = view.findViewById(R.id.btnlogindash)
        val btncuenta: Button = view.findViewById(R.id.btncuenta)
        btnlogindash.setOnClickListener(this)
        btncuenta.setOnClickListener(this)

        // Carrusel con ImageSlider
        val sliderView: ImageSlider = view.findViewById(R.id.svCarrusel)
        val slideList = arrayListOf(
            SlideModel(R.drawable.rul_img_02, ScaleTypes.FIT),
            SlideModel(R.drawable.rul_img_04, ScaleTypes.FIT),
            SlideModel(R.drawable.rul_img_05, ScaleTypes.FIT),
            SlideModel(R.drawable.rul_img_03, ScaleTypes.FIT),
            SlideModel(R.drawable.rul_img_01, ScaleTypes.FIT)
        )
        sliderView.setImageList(slideList) // ¡Listo! El carrusel se auto‑cicla solo

        // ViewModel y productos top‑seller
        productoDashViewModel = ViewModelProvider(this)[ProductoDashViewModel::class.java]
        rvTopProductos.layoutManager = LinearLayoutManager(requireContext())
        rvTopProductos.adapter = ProductoDashboardAdapter(requireContext())
        productoDashViewModel.topSellingProducts.observe(
            viewLifecycleOwner,
            Observer { products ->
                (rvTopProductos.adapter as ProductoDashboardAdapter).setProductList(products)
            }
        )

        return view
    }

    // Mostrar u ocultar opción de login según JWT en SharedPreferences
    override fun onResume() {
        super.onResume()
        val prefs = activity?.getSharedPreferences("UsuarioLogueado", Context.MODE_PRIVATE)
        val token = prefs?.getString("jwt", null)
        Log.d("DashboardFragment", "JWT: $token")

        loginOption.visibility = if (token == null) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnlogindash -> startActivity(Intent(activity, Login::class.java))
            R.id.btncuenta    -> startActivity(Intent(activity, RegisterUser::class.java))
        }
    }
}
