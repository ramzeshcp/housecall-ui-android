@file:Suppress("unused")

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.housecall.designsystem.color.HcColors

/**
 * Curated icon registry for the builder DSL. Maps string names (used in JSON
 * `leadingIcon` / `trailingIcon` props) to Material Icons **Outlined** variants.
 *
 * **Trade-off:** these are placeholder icons styled by Material (Roboto-era).
 * They are NOT byte-perfect replicas of the real `HcIcons` set used inside
 * the Android app. Once `HcIcons` is migrated into the externalized lib, this
 * registry will switch to those drawables — the JSON contract (`"search"`,
 * `"close"`, …) stays the same.
 *
 * Aliases are supported (e.g. `"back"` → `Icons.Outlined.ArrowBack`,
 * `"trash"` → `Icons.Outlined.Delete`) so designers can use natural names.
 *
 * Unknown names render `Icons.Outlined.HelpOutline` so the gap is visible
 * in the prototype, not silently swallowed.
 */
private val outlinedIconMap: Map<String, ImageVector> = mapOf(
    // search / find
    "search" to Icons.Outlined.Search,
    "find" to Icons.Outlined.Search,
    "magnify" to Icons.Outlined.Search,

    // close / dismiss
    "close" to Icons.Outlined.Close,
    "x" to Icons.Outlined.Close,
    "dismiss" to Icons.Outlined.Close,
    "cancel" to Icons.Outlined.Close,

    // navigation
    "menu" to Icons.Outlined.Menu,
    "hamburger" to Icons.Outlined.Menu,
    "back" to Icons.Outlined.ArrowBack,
    "arrow-back" to Icons.Outlined.ArrowBack,
    "forward" to Icons.Outlined.ArrowForward,
    "arrow-forward" to Icons.Outlined.ArrowForward,
    "chevron-left" to Icons.Outlined.ChevronLeft,
    "chevron-right" to Icons.Outlined.ChevronRight,
    "expand-more" to Icons.Outlined.ExpandMore,
    "expand-less" to Icons.Outlined.ExpandLess,
    "arrow-drop-down" to Icons.Outlined.ArrowDropDown,
    "arrow-drop-up" to Icons.Outlined.ArrowDropUp,
    "more-vert" to Icons.Outlined.MoreVert,
    "more-horiz" to Icons.Outlined.MoreHoriz,
    "more" to Icons.Outlined.MoreVert,

    // identity / account
    "person" to Icons.Outlined.Person,
    "user" to Icons.Outlined.Person,
    "profile" to Icons.Outlined.Person,
    "account" to Icons.Outlined.AccountCircle,

    // contact
    "email" to Icons.Outlined.Email,
    "mail" to Icons.Outlined.Email,
    "phone" to Icons.Outlined.Phone,
    "call" to Icons.Outlined.Phone,

    // security
    "lock" to Icons.Outlined.Lock,
    "password" to Icons.Outlined.Lock,
    "visibility" to Icons.Outlined.Visibility,
    "show" to Icons.Outlined.Visibility,
    "visibility-off" to Icons.Outlined.VisibilityOff,
    "hide" to Icons.Outlined.VisibilityOff,

    // crud
    "add" to Icons.Outlined.Add,
    "plus" to Icons.Outlined.Add,
    "edit" to Icons.Outlined.Edit,
    "pencil" to Icons.Outlined.Edit,
    "delete" to Icons.Outlined.Delete,
    "trash" to Icons.Outlined.Delete,
    "remove" to Icons.Outlined.Delete,

    // file / content
    "file" to Icons.Outlined.Description,
    "document" to Icons.Outlined.Description,
    "folder" to Icons.Outlined.Folder,
    "image" to Icons.Outlined.Image,
    "photo" to Icons.Outlined.Image,
    "camera" to Icons.Outlined.PhotoCamera,
    "attach" to Icons.Outlined.AttachFile,
    "paperclip" to Icons.Outlined.AttachFile,
    "copy" to Icons.Outlined.ContentCopy,
    "download" to Icons.Outlined.Download,

    // status / feedback
    "info" to Icons.Outlined.Info,
    "warning" to Icons.Outlined.Warning,
    "alert" to Icons.Outlined.Warning,
    "check" to Icons.Outlined.Check,
    "checkmark" to Icons.Outlined.Check,
    "help" to Icons.Outlined.HelpOutline,
    "question" to Icons.Outlined.HelpOutline,

    // app
    "settings" to Icons.Outlined.Settings,
    "gear" to Icons.Outlined.Settings,
    "home" to Icons.Outlined.Home,
    "notifications" to Icons.Outlined.Notifications,
    "bell" to Icons.Outlined.Notifications,
    "share" to Icons.Outlined.Share,
    "send" to Icons.Outlined.Send,
    "refresh" to Icons.Outlined.Refresh,
    "reload" to Icons.Outlined.Refresh,
    "favorite" to Icons.Outlined.Favorite,
    "heart" to Icons.Outlined.Favorite,
    "star" to Icons.Outlined.Star,
    "language" to Icons.Outlined.Language,

    // commerce
    "shopping-cart" to Icons.Outlined.ShoppingCart,
    "cart" to Icons.Outlined.ShoppingCart,
    "credit-card" to Icons.Outlined.CreditCard,
    "card" to Icons.Outlined.CreditCard,
    "money" to Icons.Outlined.AttachMoney,
    "dollar" to Icons.Outlined.AttachMoney,

    // time / location
    "calendar" to Icons.Outlined.CalendarToday,
    "date" to Icons.Outlined.DateRange,
    "date-range" to Icons.Outlined.DateRange,
    "time" to Icons.Outlined.AccessTime,
    "clock" to Icons.Outlined.AccessTime,
    "location" to Icons.Outlined.LocationOn,
    "pin" to Icons.Outlined.LocationOn,
    "map" to Icons.Outlined.LocationOn,
)

/** Names known to the registry (sorted). Used by skill docs and tests. */
internal val knownIconNames: List<String> = outlinedIconMap.keys.sorted()

/**
 * Renders the icon registered for [name], or the help/unknown icon as a
 * fallback. Designed to be wrapped in a `{ ResolveIcon(name) }` lambda when
 * passed as a slot to `HcTextField(leadingIcon = …, trailingIcon = …)`.
 */
@Composable
fun ResolveIcon(name: String) {
    val vector = outlinedIconMap[name] ?: Icons.Outlined.HelpOutline
    val tint = if (outlinedIconMap.containsKey(name)) {
        HcColors.Text.OnSurfaceSecondary
    } else {
        // Unknown icon: red tint so designers can see the gap immediately.
        HcColors.Error.Main
    }
    Icon(imageVector = vector, contentDescription = null, tint = tint)
}
