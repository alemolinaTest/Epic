# Epic
Dept Test Demo
(EARTH POLYCHROMATIC IMAGE CAMERA)

Se uso retrofit para las llamadas a Api rest
Se uso Clean Architecture (layers app(presentation),data y domain)
Se uso Jetpack Compose para la UI
Se Uso Glide Compose para mostrar las imagenes
Se Uso Navigation para la navegacion entre fragmentos
Se uso navArgs para la comunicacion de datos entre fragments (arguments)

No llegue a hacer todo, por prblemas de conexion y de tiempo.

No se hizo al player de todas las imagenes. Se planeaba usar un Pager
Ejemplo:


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductImageCarousel(
modifier: Modifier = Modifier,
listImage: List<String> = listOf()
) {
val state = rememberPagerState()
HorizontalPager(
state = state,
count = listImage.size,
modifier = modifier
) { pagerScope ->
val imagePainter = rememberAsyncImagePainter(
model = listImage[pagerScope],
error = painterResource(id = R.drawable.ic_launcher_foreground),
)
Box(contentAlignment = Alignment.BottomCenter) {
Image(
modifier = Modifier
.padding(
start = 8.dp,
end = 8.dp
)
.clip(RoundedCornerShape(10.dp))
.fillMaxSize(),
painter = imagePainter,
contentDescription = listImage[pagerScope],
contentScale = ContentScale.Crop,
)
}
}
}

