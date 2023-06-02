package pro.jsandoval.movieapp.utils.result

interface DataToDomainMapper<in I, out O> {

    fun mapToDomain(input: I): O
}
