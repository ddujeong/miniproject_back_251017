import "../pages/Reservation.css";

const MyReservation = ({ reservations, onCancel }) => {
  const getStatus = (reservation) => {
    const now = new Date();
    const startTime = new Date(reservation.reservationdatetime);
    const endTime = new Date(startTime.getTime() + 2 * 60 * 60 * 1000);
    if (reservation.status === "취소") return "취소";
    if (now >= startTime && now <= endTime) return "진행중";
    if (now > endTime) return "완료";
    return "예약";
  };

  return (
    <div className="reservation-card">
      {reservations && reservations.length === 0 ? (
        <p>예약 내역이 없습니다.</p>
      ) : (
        <table className="reservation-table">
          <thead>
            <tr>
              <th>서비스</th>
              <th>날짜</th>
              <th>시간</th>
              <th>상태</th>
              <th>액션</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map((r) => (
              <tr key={r.id}>
                <td>{r.service}</td>
                <td>
                  {r.reservationdatetime
                    ? new Date(r.reservationdatetime).toISOString().slice(0, 10)
                    : "-"}
                </td>
                <td>
                  {r.reservationdatetime
                    ? new Date(r.reservationdatetime).toTimeString().slice(0, 5)
                    : "-"}
                </td>
                <td className={getStatus(r).toLowerCase()}>{getStatus(r)}</td>
                <td>
                  {getStatus(r) === "예약" && (
                    <button
                      className="action-btn delete"
                      onClick={() => onCancel(r.id, "취소")}
                      value={"취소"}
                    >
                      취소
                    </button>
                  )}
                  {getStatus(r) === "완료" && (
                    <button
                      className="action-btn delete"
                      onClick={() => onCancel(r.id, "삭제")}
                      value={"삭제"}
                    >
                      삭제
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default MyReservation;
